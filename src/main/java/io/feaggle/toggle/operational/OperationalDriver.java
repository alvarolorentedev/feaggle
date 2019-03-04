package io.feaggle.toggle.operational;

import io.feaggle.toggle.operational.sensor.Cpu;
import io.feaggle.toggle.operational.sensor.Sensor;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

@Builder
public class OperationalDriver {
    @Singular
    public final List<Rule> rules;

    public final boolean isOperational(String toggle) {
        return rules.stream().filter(rule -> rule.toggle.equals(toggle))
                .filter(rule -> rule.enabled)
                .flatMap(rule -> rule.sensors.stream())
                .map(Sensor::evaluate)
                .reduce(true, (a, b) -> a && b);
    }

    static {
        OperationalDriver.builder()
                .rule(Rule.builder()
                        .toggle("pepe")
                        .sensor(
                                Cpu.builder().predicate(Cpu.usageIsMoreThan(80)).build()
                        )
                        .enabled(true)
                        .build());
    }
}
