/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.toggle;

import io.feaggle.driver.DrivenBy;
import io.feaggle.toggle.operational.OperationalDriver;

import java.util.concurrent.atomic.AtomicBoolean;

public class OperationalToggle implements DrivenBy<OperationalDriver> {
    private final String name;
    private final AtomicBoolean enabled;

    public OperationalToggle(String name, AtomicBoolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

    @Override
    public void drive(OperationalDriver operationalDriver) {
        this.enabled.set(operationalDriver.isOperational(name));
    }

    @Override
    public String identifier() {
        return name;
    }

    public boolean isEnabled() {
        return this.enabled.get();
    }
}
