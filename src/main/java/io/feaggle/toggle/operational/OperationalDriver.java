package io.feaggle.toggle.operational;

import io.feaggle.toggle.operational.sensor.Cpu;
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
                .map(Sensor::evaluate)
                .reduce(true, (a, b) -> a && b)).isPresent();
    }
}
