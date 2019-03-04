package io.feaggle.toggle.experiment.segment;

import io.feaggle.toggle.experiment.ExperimentCohort;

public interface Segment<Cohort extends ExperimentCohort> {
    boolean evaluate(Cohort cohort);
}
