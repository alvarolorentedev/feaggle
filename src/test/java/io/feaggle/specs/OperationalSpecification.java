package io.feaggle.specs;

import io.feaggle.DriverLoader;
import io.feaggle.Feaggle;
import io.feaggle.specs.cohort.TestCohort;
import io.feaggle.toggle.OperationalToggle;
import io.feaggle.toggle.ReleaseToggle;
import io.feaggle.toggle.experiment.ExperimentDriver;
import io.feaggle.toggle.operational.OperationalDriver;
import io.feaggle.toggle.operational.Rule;
import io.feaggle.toggle.operational.sensor.Cpu;
import io.feaggle.toggle.release.ReleaseDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    public void shouldApplyRuleWhenEnabledAndSensorsWork() {
        OperationalToggle toggle = toggleFor(Rule.builder()
                .toggle(TOGGLE_NAME)
                .enabled(true)
                .sensor(Cpu.builder().predicate(Cpu.usageIsGreaterThan(0)).build())
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
                return OperationalDriver.builder()
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
