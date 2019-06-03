/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.toggle;

import io.feaggle.driver.DrivenBy;
import io.feaggle.toggle.experiment.ExperimentCohort;
import io.feaggle.toggle.experiment.ExperimentDriver;

import java.util.concurrent.atomic.AtomicReference;

public class BasicExperimentToggle<Cohort extends ExperimentCohort> implements ExperimentToggle<Cohort>, DrivenBy<ExperimentDriver<Cohort>> {
    private final String name;
    private final AtomicReference<ExperimentDriver<Cohort>> driver;

    public BasicExperimentToggle(String name) {
        this.name = name;
        this.driver = new AtomicReference<>(null);
    }

    @Override
    public void drive(ExperimentDriver<Cohort> contextExperimentDriver) {
        driver.set(contextExperimentDriver);
    }

    @Override
    public boolean isEnabledFor(Cohort cohort) {
        return driver.get().isEnabledForCohort(name, cohort);
    }
}
