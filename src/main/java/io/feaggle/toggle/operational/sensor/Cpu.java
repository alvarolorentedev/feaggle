/*
 * Copyright (c) 2019-present, Kevin Mas Ruiz
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package io.feaggle.toggle.operational.sensor;

import lombok.Builder;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.function.Predicate;

@Builder
public class Cpu implements Sensor {
    private final Predicate<Double> predicate;

    public static Predicate<Double> usageIsGreaterThan(double load) {
        return currentLoad -> currentLoad >= load;
    }

    @Override
    public boolean evaluate() {
        return predicate.test(cpuUsage());
    }

    private double cpuUsage() {
        try {
            MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
            ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

            if (list.isEmpty())     return Double.NaN;

            Attribute att = (Attribute)list.get(0);
            Double value  = (Double)att.getValue();

            // usually takes a couple of seconds before we get real values
            if (value == -1.0)      return 0;
            // returns a percentage value with 1 decimal point precision
            return ((int)(value * 1000) / 10.0);
        } catch (Exception e) {
            return 0;
        }
    }
}
