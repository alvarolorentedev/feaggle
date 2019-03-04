feaggle
[![Build Status](https://travis-ci.org/kmruiz/feaggle.svg?branch=master)](https://travis-ci.org/kmruiz/feaggle)
[![Coverage Status](https://coveralls.io/repos/github/kmruiz/feaggle/badge.svg?branch=master)](https://coveralls.io/github/kmruiz/feaggle?branch=master)
=============================================

*feaggle* is a feature toggle library that aims to be comfortable and versatile, in a way that
it can grow with your project.

It supports three kind of toggles:

* *ExperimentToggles*: Enable switching behaviour depending on the cohort. A cohort is a subset of people, 
defined from one to several segments.
* *OperationalToggles*: Supports changing the behaviour of an application depending on the outcome of
different sensors.
* *ReleaseToggles*: Enables behaviour from a simple configuration. It can be used for enabling features under
continuous delivery.