package io.feaggle.specs.cohort;

import io.feaggle.toggle.experiment.ExperimentCohort;

public class TestCohort implements ExperimentCohort {
    private final String identifier;

    public TestCohort(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String identifier() {
        return identifier;
    }
}