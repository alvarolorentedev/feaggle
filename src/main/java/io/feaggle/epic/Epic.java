/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.epic;

import io.feaggle.toggle.ExperimentToggle;
import io.feaggle.toggle.OperationalToggle;
import io.feaggle.toggle.ReleaseToggle;
import io.feaggle.toggle.experiment.ExperimentCohort;
import lombok.Builder;
import lombok.Singular;

import java.util.List;
import java.util.stream.Stream;

@Builder
public class Epic<Cohort extends ExperimentCohort> {
    @Singular
    private final List<ReleaseToggle> releases;
    @Singular
    private final List<ExperimentToggle<Cohort>> experiments;
    @Singular
    private final List<OperationalToggle> operationals;


    public boolean isEnabledFor(Cohort cohort) {
        Stream<Boolean> releaseStatus = releases.stream().map(ReleaseToggle::isEnabled);
        Stream<Boolean> experimentStatus = experiments.stream().map(exp -> exp.isEnabledFor(cohort));
        Stream<Boolean> operationalStatus = operationals.stream().map(OperationalToggle::isEnabled);

        return Stream.concat(
                releaseStatus,
                Stream.concat(experimentStatus, operationalStatus)
        ).allMatch(e -> e == true);
    }
}
