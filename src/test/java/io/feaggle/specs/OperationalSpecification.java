/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.specs;

import io.feaggle.DriverLoader;
import io.feaggle.Feaggle;
import io.feaggle.infrastructure.Unit;
import io.feaggle.specs.cohort.TestCohort;
import io.feaggle.toggle.OperationalToggle;
import io.feaggle.toggle.experiment.ExperimentDriver;
import io.feaggle.toggle.operational.BasicOperationalDriver;
import io.feaggle.toggle.operational.OperationalDriver;
import io.feaggle.toggle.operational.Rule;
import io.feaggle.toggle.operational.sensor.Cpu;
import io.feaggle.toggle.operational.sensor.Disk;
import io.feaggle.toggle.operational.sensor.Healthcheck;
import io.feaggle.toggle.operational.sensor.Memory;
import io.feaggle.toggle.release.ReleaseDriver;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OperationalSpecification {
    private static final String TOGGLE_NAME = "toggle";

    @Test
    public void shouldRetrieveToggleName() {
        OperationalToggle toggle = toggleFor(Rule.builder()
                .toggle(TOGGLE_NAME)
                .build());

        assertEquals(TOGGLE_NAME, toggle.identifier());
    }

    @Test
    public void shouldApplyRuleWhenEnabledAndCpuSensorsEvaluate() {
        OperationalToggle toggle = toggleFor(Rule.builder()
                .toggle(TOGGLE_NAME)
                .enabled(true)
                .sensor(Cpu.builder().predicate(Cpu.usageIsGreaterThan(0)).build())
                .build());

        assertTrue(toggle.isEnabled());
    }

    @Test
    public void shouldApplyRuleWhenEnabledAndDiskSensorsEvaluate() throws IOException {
        OperationalToggle toggle = toggleFor(Rule.builder()
                .toggle(TOGGLE_NAME)
                .enabled(true)
                .sensor(Disk.builder().fileStore(Disk.fileStoreOf(Paths.get("."))).predicate(Disk.spaceAvailableIsLessThan(20, Unit.TB)).build())
                .build());

        assertTrue(toggle.isEnabled());
    }

    @Test
    public void shouldApplyRuleWhenEnabledAndMemorySensorsEvaluate() throws IOException {
        OperationalToggle toggle = toggleFor(Rule.builder()
                .toggle(TOGGLE_NAME)
                .enabled(true)
                .sensor(Memory.builder().predicate(Memory.usageIsGreaterThan(1, Unit.KB)).build())
                .build());

        assertTrue(toggle.isEnabled());
    }

    @Test
    public void shouldApplyRuleWhenEnabledAndMemoryPercentageSensorsEvaluate() throws IOException {
        OperationalToggle toggle = toggleFor(Rule.builder()
                .toggle(TOGGLE_NAME)
                .enabled(true)
                .sensor(Memory.builder().predicate(Memory.usageIsGreaterThan(1)).build())
                .build());

        assertTrue(toggle.isEnabled());
    }

    @Test
    public void shouldApplyRuleWhenEnabledAndHealtcheckEvaluate() throws IOException {
        OperationalToggle toggle = toggleFor(Rule.builder()
                .toggle(TOGGLE_NAME)
                .enabled(true)
                .sensor(Healthcheck.builder()
                        .check(() -> CompletableFuture.completedFuture(true))
                        .interval(1)
                        .healthyCount(1)
                        .unhealthyCount(1)
                        .build())
                .build());

        assertTrue(toggle.isEnabled());
    }

    private OperationalToggle toggleFor(Rule rule) {
        return Feaggle.load(new DriverLoader<TestCohort>() {
            @Override
            public ExperimentDriver<TestCohort> loadExperimentDriver() {
                return null;
            }

            @Override
            public OperationalDriver loadOperationalDriver() {
                return BasicOperationalDriver.builder()
                        .rule(rule)
                        .build();
            }

            @Override
            public ReleaseDriver loadReleaseDriver() {
                return null;
            }
        }).operational(TOGGLE_NAME);
    }
}
