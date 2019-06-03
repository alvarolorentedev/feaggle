/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.toggle;

import io.feaggle.toggle.experiment.ExperimentCohort;

public interface ExperimentToggle<Cohort extends ExperimentCohort> {
    boolean isEnabledFor(Cohort cohort);
}
