package io.feaggle;

import io.feaggle.toggle.ExperimentToggle;
import io.feaggle.toggle.experiment.Experiment;
import io.feaggle.toggle.experiment.ExperimentCohort;
import io.feaggle.toggle.experiment.ExperimentDriver;
import io.feaggle.toggle.experiment.segment.Rollout;
import io.feaggle.toggle.operational.OperationalDriver;
import io.feaggle.toggle.release.BasicReleaseDriver;
import io.feaggle.toggle.release.ReleaseDriver;

public class Test {
    public static class VeryBasicCohort implements ExperimentCohort {
        private final long id;

        public VeryBasicCohort(long id) {
            this.id = id;
        }

        @Override
        public String identifier() {
            return String.valueOf(id);
        }
    }

    public static void main(String[] args) {
        DriverLoader<VeryBasicCohort> loader = new DriverLoader<VeryBasicCohort>() {
            @Override
            public ExperimentDriver loadExperimentDriver() {
                return ExperimentDriver.builder()
                        .experiment(
                                Experiment.builder()
                                        .toggle("test")
                                        .enabled(true)
                                        .segment(
                                                Rollout.builder().percentage(50).build()
                                        )
                                        .build()
                        )
                        .build();
            }

            @Override
            public OperationalDriver loadOperationalDriver() {
                return OperationalDriver.builder().build();
            }

            @Override
            public ReleaseDriver loadReleaseDriver() {
                return BasicReleaseDriver.builder().build();
            }
        };

        Feaggle<VeryBasicCohort> feaggle = Feaggle.load(loader);
        ExperimentToggle<VeryBasicCohort> test = feaggle.experiment("test");
        int hit = 0;

        for (int i = 0; i < 100; i++) {
            if (test.isEnabledFor(new VeryBasicCohort(i))) {
                hit++;
            }
        }

        System.out.println("Hit " + hit + " times");
    }
}
