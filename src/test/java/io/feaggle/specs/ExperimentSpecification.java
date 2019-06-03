/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.specs;

import io.feaggle.DriverLoader;
import io.feaggle.Feaggle;
import io.feaggle.specs.cohort.TestCohort;
import io.feaggle.toggle.ExperimentToggle;
import io.feaggle.toggle.experiment.BasicExperimentDriver;
import io.feaggle.toggle.experiment.Experiment;
import io.feaggle.toggle.experiment.ExperimentDriver;
import io.feaggle.toggle.experiment.segment.Rollout;
import io.feaggle.toggle.operational.OperationalDriver;
import io.feaggle.toggle.release.ReleaseDriver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExperimentSpecification {
    private static final String EXPERIMENT_NAME = "experiment";
    private static final String IDENTIFIER = "identifier";
    private static final TestCohort COHORT = new TestCohort(IDENTIFIER);

    @Test
    public void shouldNotHitWhenDisabled() {
        ExperimentToggle<TestCohort> experiment = toggleFor(Experiment.<TestCohort>builder()
                .toggle(EXPERIMENT_NAME)
                .enabled(false)
                .build()
        );

        assertFalse(experiment.isEnabledFor(COHORT));
    }

    @Test
    public void shouldHitWhenEnabled() {
        ExperimentToggle<TestCohort> experiment = toggleFor(Experiment.<TestCohort>builder()
                .toggle(EXPERIMENT_NAME)
                .segment(Rollout.<TestCohort>builder().percentage(100).build())
                .enabled(true)
                .build()
        );

        assertTrue(experiment.isEnabledFor(COHORT));
    }

    @Test
    public void rollOutShouldDistributeEvenLoad() {
        ExperimentToggle<TestCohort> experiment = toggleFor(Experiment.<TestCohort>builder()
                .toggle(EXPERIMENT_NAME)
                .segment(Rollout.<TestCohort>builder().percentage(50).build())
                .enabled(true)
                .build()
        );

        final int total = 100;
        int hits = 0;

        for (int i = 0; i < total; i++) {
            hits += (experiment.isEnabledFor(COHORT) ? 1 : 0);
        }

        assertEquals(50, hits, 10);
    }

    @Test
    public void rollOutShouldStickLoad() {
        ExperimentToggle<TestCohort> experiment = toggleFor(Experiment.<TestCohort>builder()
                .toggle(EXPERIMENT_NAME)
                .segment(Rollout.<TestCohort>builder().sticky(true).percentage(25).build())
                .enabled(true)
                .build()
        );

        final int total = 100;
        int hits = 0;

        for (int i = 0; i < total; i++) {
            hits += (experiment.isEnabledFor(COHORT) ? 1 : 0);
        }

        assertTrue(hits == 0 || hits == 100, "should not hit never or hit always");
    }

    private ExperimentToggle<TestCohort> toggleFor(Experiment<TestCohort> experiment) {
        return Feaggle.load(new DriverLoader<TestCohort>() {
            @Override
            public ExperimentDriver<TestCohort> loadExperimentDriver() {
                return BasicExperimentDriver.<TestCohort>builder()
                        .experiment(experiment)
                        .build();
            }

            @Override
            public OperationalDriver loadOperationalDriver() {
                return null;
            }

            @Override
            public ReleaseDriver loadReleaseDriver() {
                return null;
            }
        }).experiment(EXPERIMENT_NAME);
    }
}