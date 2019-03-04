package io.feaggle.toggle.experiment;

import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public class ExperimentDriver<Cohort extends ExperimentCohort> {
    @Singular
    private final List<Experiment<Cohort>> experiments;

    public boolean isEnabledForCohort(String experimentName, Cohort cohort) {
        return experiments.stream().filter(experiment -> experiment.toggle.equals(experimentName))
                .filter(experiment -> experiment.enabled)
                .flatMap(experiment -> experiment.segments.stream())
                .map(segment -> segment.evaluate(cohort))
                .reduce(true, (a, b) -> a && b);
    }
}
