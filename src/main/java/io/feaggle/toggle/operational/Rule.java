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
