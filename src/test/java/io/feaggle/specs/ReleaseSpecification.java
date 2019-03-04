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
import io.feaggle.toggle.ReleaseToggle;
import io.feaggle.toggle.experiment.ExperimentDriver;
import io.feaggle.toggle.operational.OperationalDriver;
import io.feaggle.toggle.release.BasicReleaseDriver;
import io.feaggle.toggle.release.ReleaseDriver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReleaseSpecification {
    private static final String RELEASE_NAME = "release";

    @Test
    public void shouldRetrieveTheToggleName() {
        ReleaseToggle toggle = toggleFor(BasicReleaseDriver.builder()
                .release(RELEASE_NAME, true)
                .build()
        );

        assertEquals(RELEASE_NAME, toggle.identifier());
    }

    @Test
    public void shouldBeEnabledWithTheRelease() {
        ReleaseToggle toggle = toggleFor(BasicReleaseDriver.builder()
                .release(RELEASE_NAME, true)
                .build()
        );

        assertTrue(toggle.isEnabled());
    }

    @Test
    public void shouldBeDisabledWithTheRelease() {
        ReleaseToggle toggle = toggleFor(BasicReleaseDriver.builder()
                .release(RELEASE_NAME, false)
                .build()
        );

        assertFalse(toggle.isEnabled());
    }

    private ReleaseToggle toggleFor(ReleaseDriver driver) {
        return Feaggle.load(new DriverLoader<TestCohort>() {
            @Override
            public ExperimentDriver<TestCohort> loadExperimentDriver() {
                return null;
            }

            @Override
            public OperationalDriver loadOperationalDriver() {
                return null;
            }

            @Override
            public ReleaseDriver loadReleaseDriver() {
                return driver;
            }
        }).release(RELEASE_NAME);
    }
}
