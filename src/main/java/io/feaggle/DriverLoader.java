/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle;

import io.feaggle.toggle.experiment.ExperimentCohort;
import io.feaggle.toggle.experiment.ExperimentDriver;
import io.feaggle.toggle.operational.OperationalDriver;
import io.feaggle.toggle.release.ReleaseDriver;

public interface DriverLoader<Cohort extends ExperimentCohort> {
    ExperimentDriver<Cohort> loadExperimentDriver();
    OperationalDriver loadOperationalDriver();
    ReleaseDriver loadReleaseDriver();
}
