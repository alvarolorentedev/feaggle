package io.feaggle.toggle.operational.sensor;

import io.feaggle.infrastructure.Unit;
import lombok.Builder;

import java.util.function.Predicate;

import static java.lang.Runtime.getRuntime;

@Builder
public class Memory implements Sensor {
    private final Predicate<Long> predicate;

    public static Predicate<Long> usageIsGreaterThan(int memory, Unit unit) {
        return usage -> usage >= (memory * unit.conversion);
    }

    public static Predicate<Long> usageIsGreaterThan(double percentage) {
        return usage -> usedPercentage(usage) >= percentage;
    }

    @Override
    public boolean evaluate() {
        return predicate.test(getRuntime().totalMemory() - getRuntime().freeMemory());
    }

    private static double usedPercentage(long usage) {
        return ((double) usage / (double) getRuntime().totalMemory()) * 100.0;
    }
}
