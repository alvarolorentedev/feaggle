/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.toggle.experiment;

import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Builder
public class ExperimentDriver<Cohort extends ExperimentCohort> {
    @Singular
    private final List<Experiment<Cohort>> experiments;

    public boolean isEnabledForCohort(String experimentName, Cohort cohort) {
        Optional<Experiment<Cohort>> maybeExperiment = experiments.stream()
                .filter(exp -> exp.toggle.equals(experimentName))
                .filter(exp -> exp.enabled)
                .findFirst();

        return maybeExperiment.filter(cohortExperiment -> Stream.of(cohortExperiment)
                .flatMap(experiment -> experiment.segments.stream())
                .allMatch(segment -> segment.evaluate(cohort))).isPresent();

    }
}
