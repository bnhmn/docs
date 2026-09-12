# CI/CD

This directory contains templates for different CI systems. Currently, templates are available for:

* [github-actions](github-actions)
* [google-cloud-build](google-cloud-build)

## What is CI/CD?

Continuous Integration (CI) is a practice where developers regularly merge their code changes into a shared repository
(often several times a day). Each change triggers an automated process that builds the application and runs tests.
The goal is to detect integration issues early, improve code quality, and ensure that the software is always in a
working state.

Continuous Delivery/Deployment (CD) extends this by automating what happens after a successful build and test:

* Continuous Delivery ensures that code is always in a releasable state and can be deployed to production at any time,
  usually with a manual approval step.
* Continuous Deployment goes one step further by automatically deploying every successful change to production without
  manual intervention.

Both aim to make releasing software faster, safer, and more reliable.

## References

* <https://martinfowler.com/bliki/ContinuousDelivery.html>
* <https://dora.dev/capabilities/continuous-integration>
* <https://dora.dev/capabilities/continuous-delivery>
