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
import java.util.Optional;
import java.util.stream.Stream;

@Builder
public class OperationalDriver {
    @Singular
    public final List<Rule> rules;

    public final boolean isOperational(String toggle) {
        Optional<Rule> maybeRule = rules
                .stream()
                .filter(rule -> rule.toggle.equals(toggle))
                .filter(rule -> rule.enabled)
                .findFirst();

        if (!maybeRule.isPresent()) {
            return false;
        }

        return maybeRule.filter(expectedRule -> Stream.of(expectedRule)
                .flatMap(rule -> rule.sensors.stream())
                .allMatch(Sensor::evaluate)).isPresent();
    }
}
