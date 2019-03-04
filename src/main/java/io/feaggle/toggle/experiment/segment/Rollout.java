/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.toggle.experiment.segment;

import io.feaggle.toggle.experiment.ExperimentCohort;
import lombok.Builder;

@Builder
public class Rollout<Cohort extends ExperimentCohort> implements Segment<Cohort> {
    private final int percentage;
    private final boolean sticky;

    @Override
    public boolean evaluate(Cohort cohort) {
        if (sticky) {
            return (cohort.identifier().hashCode() % 100) < percentage;
        } else {
            return (Math.random() * 100) < percentage;
        }
    }
}
