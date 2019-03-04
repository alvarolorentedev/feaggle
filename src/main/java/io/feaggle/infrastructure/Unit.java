package io.feaggle.infrastructure;

public enum Unit {
    B(1),
    KB(1000),
    MB(1000000),
    GB(1000000000),
    TB(1000000000000L);

    public final long conversion;

    Unit(long conversion) {
        this.conversion = conversion;
    }
}