/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle;

import io.feaggle.toggle.experiment.BasicExperimentDriver;
import io.feaggle.toggle.experiment.ExperimentCohort;
import io.feaggle.toggle.experiment.ExperimentDriver;
import io.feaggle.toggle.operational.BasicOperationalDriver;
import io.feaggle.toggle.operational.OperationalDriver;
import io.feaggle.toggle.release.BasicReleaseDriver;
import io.feaggle.toggle.release.ReleaseDriver;
import lombok.Builder;

@Builder
public class BasicDriverLoader<Cohort extends ExperimentCohort> implements DriverLoader<Cohort> {
    private final BasicExperimentDriver<Cohort> experiments;
    private final BasicOperationalDriver operationals;
    private final BasicReleaseDriver releases;

    @Override
    public ExperimentDriver<Cohort> loadExperimentDriver() {
        return experiments;
    }

    @Override
    public OperationalDriver loadOperationalDriver() {
        return operationals;
    }

    @Override
    public ReleaseDriver loadReleaseDriver() {
        return releases;
    }
}
