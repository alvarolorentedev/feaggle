package io.feaggle.toggle.operational.sensor;

import lombok.Builder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public class Healthcheck implements Sensor {
    private final Supplier<CompletableFuture<Boolean>> check;
    private final long interval;
    private final long healthyCount;
    private final long unhealthyCount;
    private final AtomicLong healthTicks;
    private final AtomicLong unhealthTicks;
    private final AtomicBoolean healthy;
    private final Timer timer;

    @Builder
    public Healthcheck(Supplier<CompletableFuture<Boolean>> check, long interval, long healthyCount, long unhealthyCount) {
        this.check = check;
        this.interval = interval;
        this.healthyCount = healthyCount;
        this.unhealthyCount = unhealthyCount;

        this.healthTicks = new AtomicLong(0);
        this.unhealthTicks = new AtomicLong(0);
        this.healthy = new AtomicBoolean(true);

        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                check();
            }
        }, 0, this.interval);
    }

    @Override
    public boolean evaluate() {
        return healthy.get();
    }

    private void check() {
        check.get().thenAccept(status -> {
            if (status) {
                healthTicks.incrementAndGet();
                unhealthTicks.set(0);
            } else {
                unhealthTicks.incrementAndGet();
                healthTicks.set(0);
            }

            if (healthTicks.get() > healthyCount) {
                healthy.set(true);
            }

            if (unhealthTicks.get() > unhealthyCount) {
                healthy.set(false);
            }
        });
    }
}
