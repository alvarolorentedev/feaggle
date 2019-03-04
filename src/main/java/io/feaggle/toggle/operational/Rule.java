/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.toggle.operational;

import io.feaggle.toggle.operational.sensor.Sensor;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public class Rule {
    public final String toggle;
    public final boolean enabled;
    @Singular
    public final List<Sensor> sensors;
}
