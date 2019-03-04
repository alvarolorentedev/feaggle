package io.feaggle.toggle.operational.sensor;

import io.feaggle.infrastructure.Unit;
import lombok.Builder;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

@Builder
public class Disk implements Sensor {
    private final Predicate<Long> predicate;
    private final FileStore fileStore;

    public static Predicate<Long> spaceAvailableIsLessThan(long size, Unit unit) {
        return (state) -> state < (size * unit.conversion);
    }

    public static FileStore fileStoreOf(Path path) throws IOException {
        return Files.getFileStore(path);
    }

    @Override
    public boolean evaluate() {
        try {
            return predicate.test(fileStore.getUsableSpace());
        } catch (IOException e) {
            return false;
        }
    }
}
