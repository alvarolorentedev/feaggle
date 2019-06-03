/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle;

import io.feaggle.toggle.*;
import io.feaggle.toggle.experiment.ExperimentCohort;
import io.feaggle.toggle.experiment.ExperimentDriver;
import io.feaggle.toggle.operational.OperationalDriver;
import io.feaggle.toggle.release.ReleaseDriver;

import java.util.concurrent.atomic.AtomicBoolean;

public class Feaggle<Cohort extends ExperimentCohort> {
    private final ExperimentDriver<Cohort> experimentDriver;
    private final OperationalDriver operationalDriver;
    private final ReleaseDriver releaseDriver;

    private Feaggle(DriverLoader<Cohort> loader) {
        experimentDriver = loader.loadExperimentDriver();
        operationalDriver = loader.loadOperationalDriver();
        releaseDriver = loader.loadReleaseDriver();
    }

    public static <Cohort extends ExperimentCohort> Feaggle<Cohort> load(DriverLoader<Cohort> loader) {
        return new Feaggle<>(loader);
    }

    public ReleaseToggle release(String name) {
        BasicReleaseToggle toggle = new BasicReleaseToggle(name, new AtomicBoolean(false));
        toggle.drive(releaseDriver);
        return toggle;
    }

    public OperationalToggle operational(String name) {
        BasicOperationalToggle toggle = new BasicOperationalToggle(name, new AtomicBoolean(false));
        toggle.drive(operationalDriver);
        return toggle;
    }

    public ExperimentToggle<Cohort> experiment(String name) {
        BasicExperimentToggle<Cohort> toggle = new BasicExperimentToggle<>(name);
        toggle.drive(experimentDriver);
        return toggle;
    }
}
