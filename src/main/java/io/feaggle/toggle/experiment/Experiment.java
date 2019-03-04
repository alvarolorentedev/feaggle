package io.feaggle.toggle.experiment;

import io.feaggle.toggle.experiment.segment.Segment;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public class Experiment<Cohort extends ExperimentCohort> {
    public final String toggle;
    public final boolean enabled;
    @Singular
    public final List<Segment<Cohort>> segments;
}
