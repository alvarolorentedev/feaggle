/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.toggle.experiment;

public interface ExperimentDriver<Cohort extends ExperimentCohort> {
    boolean isEnabledForCohort(String experimentName, Cohort cohort);
}
