# Automatic Dependency Updates

You can use tools like Renovate and Dependabot to automate dependency updates in software projects. A dependency
is a separate piece of software that your project relies on to run correctly. These tools can detect when your
dependencies have updates available, and automatically generate the necessary changes in your project.
Instead of manually checking and updating your dependencies, these tools do it automatically.
This can save you lots of time and helps to keep your project secure.

<img width="450px" alt="Dependency Update Process" src="https://senacor.blog/wp-content/uploads/2024/09/Process-software-dependency-bots.png">

<https://senacor.blog/how-to-renovate-why-and-how-you-should-use-automated-dependency-updates-in-your-software-projects>

## Tools

* [Renovate](./renovate/) - A dependency update tool that is highly customizable and runs on many platforms.
* [Dependabot](https://docs.github.com/en/code-security/getting-started/dependabot-quickstart-guide) -  A dependency update tool built into GitHub that is easy to setup.

## How it Works

1. The tool scans the repository and identifies all dependencies.
2. The tool checks for available updates of those dependencies.
3. The tool creates a feature branch and updates the version of the dependencies there.
4. The tool creates a pull request to merge the changes into the main branch.
5. The build system runs the repository's automated tests.

If the tests are successful

6. The developer approves the changes and merges the pull request.

If the tests fail

6. The developer manually analyzes how the error can be resolved.
7. The developer fixes the error on the feature branch.
8. Another developer reviews the changes.
9. The developer merges the pull request.

## Pull Request Frequency

By default, a separate pull request is created for each dependency update. This makes it easy to trace the changes. If tests fail, you can immediately see which dependency is the cause.

However, a large number of pull requests can lead to high maintenance effort for developers, as they need to review each pull request individually.
The frequency can be adjusted through two parameters:

* How often the dependency check runs
* How granular the pull requests are

To keep the number of necessary reviews low, you can group dependencies, so that several of them are updated in one pull request.

By establishing a regular schedule to update all dependencies (for example, biweekly or monthly), you can further reduce the workload.

Additionally, you can set up auto-merge to automatically merge the pull requests if the tests were successful.
