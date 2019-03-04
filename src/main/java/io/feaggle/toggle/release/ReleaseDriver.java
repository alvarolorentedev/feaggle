package io.feaggle.toggle.release;

public interface ReleaseDriver {
    boolean isFlaggedForRelease(String feature);
}
