/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.specs.epic;

import io.feaggle.epic.Epic;
import io.feaggle.toggle.BasicExperimentToggle;
import io.feaggle.toggle.BasicOperationalToggle;
import io.feaggle.toggle.BasicReleaseToggle;
import io.feaggle.toggle.experiment.ExperimentCohort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EpicSpecification {
    private Epic<ExperimentCohort> epic;
    private BasicReleaseToggle releaseToggle;
    private BasicExperimentToggle<ExperimentCohort> experimentToggle;
    private BasicOperationalToggle operationalToggle;
    private ExperimentCohort cohort;

    @BeforeEach
    void setUp() {
        cohort = mock(ExperimentCohort.class);
        releaseToggle = mock(BasicReleaseToggle.class);
        experimentToggle = mock(BasicExperimentToggle.class);
        operationalToggle = mock(BasicOperationalToggle.class);

        epic = Epic.builder()
                .release(releaseToggle)
                .experiment(experimentToggle)
                .operational(operationalToggle)
                .build();
    }

    @Test
    void shouldBeOnIfAllTogglesAreOn() {
        when(releaseToggle.isEnabled()).thenReturn(true);
        when(experimentToggle.isEnabledFor(cohort)).thenReturn(true);
        when(operationalToggle.isEnabled()).thenReturn(true);

        assertTrue(epic.isEnabledFor(cohort));
    }

    @Test
    void shouldBeOffIfAReleaseIsDisabled() {
        when(releaseToggle.isEnabled()).thenReturn(false);
        when(experimentToggle.isEnabledFor(cohort)).thenReturn(true);
        when(operationalToggle.isEnabled()).thenReturn(true);

        assertFalse(epic.isEnabledFor(cohort));
    }

    @Test
    void shouldBeOffIfAnExperimentIsDisabled() {
        when(releaseToggle.isEnabled()).thenReturn(true);
        when(experimentToggle.isEnabledFor(cohort)).thenReturn(false);
        when(operationalToggle.isEnabled()).thenReturn(true);

        assertFalse(epic.isEnabledFor(cohort));
    }

    @Test
    void shouldBeOffIfAnOperationalToggleIsDisabled() {
        when(releaseToggle.isEnabled()).thenReturn(true);
        when(experimentToggle.isEnabledFor(cohort)).thenReturn(true);
        when(operationalToggle.isEnabled()).thenReturn(false);

        assertFalse(epic.isEnabledFor(cohort));
    }
}
