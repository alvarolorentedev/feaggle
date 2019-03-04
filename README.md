feaggle
[![Build Status](https://travis-ci.org/kmruiz/feaggle.svg?branch=master)](https://travis-ci.org/kmruiz/feaggle)
[![Coverage Status](https://coveralls.io/repos/github/kmruiz/feaggle/badge.svg?branch=master)](https://coveralls.io/github/kmruiz/feaggle?branch=master)
=============================================

*feaggle* is a feature toggle library that aims to be comfortable and versatile, in a way that
it can grow with your project.

It supports three kind of toggles:

* *Release Toggles*: Enables behaviour from a simple configuration. It can be used for enabling features under
continuous delivery.
* *Experiment Toggles*: Enable switching behaviour depending on the cohort. A cohort is a subset of people, 
defined from one to several segments.
* *Operational Toggles*: Supports changing the behaviour of an application depending on the outcome of
different sensors.

## Release Toggles

Release toggles depend on a ReleaseDriver, that will hold the information of each release and will update
the toggle information when need. This, for example, allows to reload toggle information from external sources.

*feaggle* comes with a *BasicReleaseDriver* that allows to declare feature toggles using a builder. An example
would be:

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

