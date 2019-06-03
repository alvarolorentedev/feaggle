# ![feaggle_jvm](https://user-images.githubusercontent.com/3071208/53872344-531a8200-3ffe-11e9-8563-f581f83c1697.png)

[![Build Status](https://travis-ci.org/feaggle/feaggle.svg?branch=master)](https://travis-ci.org/feaggle/feaggle)
[![Coverage Status](https://coveralls.io/repos/github/kmruiz/feaggle/badge.svg?branch=master)](https://coveralls.io/github/feaggle/feaggle?branch=master)
[ ![Download](https://api.bintray.com/packages/kmruiz/feaggle/feaggle/images/download.svg) ](https://bintray.com/kmruiz/feaggle/feaggle/_latestVersion)


*feaggle* is a feature toggle library that aims to be comfortable and versatile, in a way that
it can grow with your project.

It supports three kind of toggles:

* *Release Toggles*: Enables behaviour from a simple configuration. It can be used for enabling features under
continuous delivery.
* *Experiment Toggles*: Enable switching behaviour depending on the cohort. A cohort is a subset of people, 
defined from one to several segments.
* *Operational Toggles*: Supports changing the behaviour of an application depending on the outcome of
different sensors.

## Gradle Dependency

You will need to use jcenter as a repository:

```groovy
repositories {
    //... 
    jcenter()
    //...
}
```

And the dependency:

```groovy
compile "io.feaggle:feaggle:2.0.0"
```


## Getting a feaggle instance

`feaggle` needs a [DriverLoader](src/main/java/io/feaggle/DriverLoader.java) (which is an interface) that will set up all the required drivers for the correct
behaviour of the library.

Usually you would create your own DriverLoader, that will build all needed drivers in memory. However, `DriverLoader` is
a good extension point and would allow you to implement your own extensions for `feaggle`, like reading the configuration
from a file or from an external service.

An example DriverLoader:

```java
Feaggle<ExperimentCohort> feaggle = Feaggle.load(
    BasicDriverLoader.builder()
            .releases(BasicReleaseDriver.builder()
                     .release(RELEASE_NAME, true)
                     .build();)
            .experiments(
                    BasicExperimentDriver.<Cohort>builder()
                        .experiment(
                            Experiment.<Cohort>builder()
                                .toggle(MY_EXPERIMENT)
                                .segment(cohort -> cohort.countryCode == "P")
                                .segment(Rollout.<Cohort>builder().percentage(50).sticky(true).build())
                                .enabled(true)
                                .build()
                        )
                        .build()
            )
            .operationals(OperationalDriver.builder()
                     .rule(
                         Rule.builder()
                             .toggle(TOGGLE_NAME)
                             .enabled(true)
                             .sensor(Healthcheck.builder()
                                     .check(this::reportingServiceAvailability)
                                     .interval(1000) // interval in milliseconds between calls
                                     .healthyCount(1) // nr of times the healthcheck should be working for being considered healthy
                                     .unhealthyCount(1) // nr of times the healthcheck should fail for being considered unhealthy
                                     .build())
                             .build()
                     ).build())
            .build()
        );
```

## Release Toggles

Release toggles depend on a ReleaseDriver, that will hold the information of each release and will update
the toggle information when need. This, for example, allows to reload toggle information from external sources.

*feaggle* comes with a [BasicReleaseDriver](src/main/java/io/feaggle/toggle/release/BasicReleaseDriver.java) that allows to 
declare feature toggles using a builder. An example would be:

```java
BasicReleaseDriver.builder()
    .release(RELEASE_NAME, true)
    .build()
```

You can add more releases by calling the `release` method in the builder more than once.

```java
BasicReleaseDriver.builder()
                .release(RELEASE_NAME_1, true)
                .release(RELEASE_NAME_2, false)
                .build()
```

You can get the release toggle directly using the `release` method in feaggle.

`feaggle.release(RELEASE_NAME_1).isEnabled()`

## Experiment Toggles

Experiment toggles require a ExperimentDriver, that will set up all experiments and cohorts for that experiment.
A cohort is a subset of the target customers, and is defined by a POJO that implements the
[ExperimentCohort interface](src/main/java/io/feaggle/toggle/experiment/ExperimentCohort.java). A Cohort is usually built with
the user information that is needed for segmenting users, and it depends on the business context.

Good candidates for segmentation are:

* Paid user / Free user
* Country

For example, we can build the Cohort class as:

```java
class Cohort implements ExperimentCohort {
    public final String identifier; // user id
    public final boolean isPaid;
    public final String countryCode;
    
    // ... constructor
    
    @Override
    public String identifier() {
        return identifier;
    }
}
```

Then you can build experiments based on that Cohort information. For example, enable the feature for all
users that are in Portugal.

```java
Experiment.<Cohort>builder()
    .toggle(MY_EXPERIMENT)
    .segment(cohort -> cohort.countryCode == "P")
    .enabled(true)
    .build()
```

You also might want to segment more your cohort, for example, only to half of your customers in Portugal.

```java
Experiment.<Cohort>builder()
    .toggle(MY_EXPERIMENT)
    .segment(cohort -> cohort.countryCode == "P")
    .segment(Rollout.<Cohort>builder().percentage(50).build())
    .enabled(true)
    .build()
```

And also you might want to stick users to the experiment, so a user that has been activated, is still activated in
future calls to the feature toggle.

```java
Experiment.<Cohort>builder()
    .toggle(MY_EXPERIMENT)
    .segment(cohort -> cohort.countryCode == "P")
    .segment(Rollout.<Cohort>builder().percentage(50).sticky(true).build())
    .enabled(true)
    .build()
```

Stickiness depend on the hashCode of the identifier, so it needs to be consistent (usually the
identifier hashCode is consistent across JVMs).

For accessing a experiment toggle from a feaggle instance, you will need to use the `experiment` method.

`feaggle.experiment(MY_EXPERIMENT).isEnabledFor(myCohortInstance)`

## Operational Toggles

Operational toggles allow to enable features based on rules. Those rules use sensors to get operational
information and decide the status of the toggle. Those sensors are categorised in:

* [Cpu](src/main/java/io/feaggle/toggle/operational/sensor/Cpu.java) sensors.
* [Disk](src/main/java/io/feaggle/toggle/operational/sensor/Disk.java) sensors.
* [Memory](src/main/java/io/feaggle/toggle/operational/sensor/Memory.java) sensors.
* [Healthchecks](src/main/java/io/feaggle/toggle/operational/sensor/Healthcheck.java).

You usually build your rules in a [OperationalDriver](src/main/java/io/feaggle/toggle/operational/OperationalDriver.java)
that will receive the rules that you want to define. An example would be:

```java
OperationalDriver.builder()
.rule(
    Rule.builder()
        .toggle(TOGGLE_NAME)
        .enabled(true)
        .sensor(Healthcheck.builder()
                .check(this::reportingServiceAvailability)
                .interval(1000) // interval in milliseconds between calls
                .healthyCount(1) // nr of times the healthcheck should be working for being considered healthy
                .unhealthyCount(1) // nr of times the healthcheck should fail for being considered unhealthy
                .build())
        .build()
).build()
```

As in Experiments, you can also add more than one sensor to a rule, enabling more complex rules (for example, enable
reports if the report service is up and there is enough disk space).

You can get the toggle from a feaggle instance calling the `operational` method.

`feaggle.operational(TOGGLE_NAME).isEnabled()`
