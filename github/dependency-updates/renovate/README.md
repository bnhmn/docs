# Renovate

<img width="80px" src="https://docs.renovatebot.com/assets/images/logo.png" alt="Renovate Logo">

You can use [Renovate](https://docs.renovatebot.com/) to automatically update software dependencies.
Renovate is an automated tool that scans software repositories for dependencies, checks whether newer versions of those dependencies are available, and then creates pull requests with these updates in the project's codebase.
Renovate needs to be set up and configured one-time for each repository. During this process, a job must be created in the build system which will run Renovate at regular intervals.

## How to Run

You can run Renovate either via [Node](https://nodejs.org/en) (preferred):

```bash
npx renovate
```

or via [Docker](https://docs.docker.com/get-docker/):

```bash
docker run renovate/renovate
```

## How to Debug

Renovate typically operates on the repository's main branch and retrieves all data via the
[platform](https://docs.renovatebot.com/modules/platform/)'s API (e.g., GitHub, GitLab, or Bitbucket).
However, this makes it difficult to test a repository configuration before merging it into the main branch.

However, there are two ways to test a configuration locally:

1. Modify the configuration using [environment variables](https://docs.renovatebot.com/configuration-options/#packagerules):

   ```bash
   export LOG_LEVEL=DEBUG
   export RENOVATE_PLATFORM=github
   export RENOVATE_TOKEN="$GITHUB_TOKEN"
   export RENOVATE_AUTODISCOVER=true
   export RENOVATE_PACKAGE_RULES='[]'
   npx renovate
   ```

2. Modify the renovate.json configuration file and run Renovate in [local mode](https://docs.renovatebot.com/modules/platform/local/):

   ```bash
   export LOG_LEVEL=DEBUG
   export RENOVATE_PLATFORM=local
   export RENOVATE_GITHUB_COM_TOKEN="$GITHUB_TOKEN"
   npx renovate
   ```

## Configuration

Renovate needs to be configured at two levels:
The [repository configuration](https://docs.renovatebot.com/configuration-options/)
and the [bot configuration](https://docs.renovatebot.com/self-hosted-configuration/).

### Repository Configuration

The repository configuration is located in the software repository, whose dependencies are being tracked,
and it solely concerns this repository. There, for example, the granularity of the pull requests can be adjusted.
The repository can be configured via a [renovate.json](https://docs.renovatebot.com/configuration-options/) file:

```json
{
  "$schema": "https://docs.renovatebot.com/renovate-schema.json",
  "description": "See https://docs.renovatebot.com/configuration-options for available options",
  "ignorePaths": [
    ".github/workflows/**"
  ]
}
```

It's possible to store reusable configuration snippets like these in a central repository:

* [default](default.json)
* [maven](maven.json)
* [npm](npm.json)
* [regex](regex.json)
* [release-notes](release-notes.json)

For example, if you want to activate the `renovate/default.json` snippet from repository `my-github-org/my-repo`, you can do so using the extends directive:

```json
{
  "$schema": "https://docs.renovatebot.com/renovate-schema.json",
  "description": "See https://docs.renovatebot.com/configuration-options for available options",
  "extends": [
    "github>my-github-org/my-repo//renovate/default"
  ],
  "ignorePaths": [
    ".github/workflows/**"
  ]
}
```

### Manage Dependencies in Text Files

Renovate automatically detects dependencies from [common package managers](https://docs.renovatebot.com/modules/manager/)
such as Maven, Gradle, npm and pip.

If you want to track dependencies that are not in a format that is supported out of the box, or if you need more
flexibility, you can use so-called [custom regex managers](https://docs.renovatebot.com/modules/manager/regex/).
They allow you to track and update dependencies in any text file using Renovate.

When using [this regex manager](regex.json), you simply need to add a dependency directive comment above the line
containing the current version number:

In shell scripts and YAML files:

```bash
# renovate: datasource=maven depName=org.sonarsource.scanner.maven:sonar-maven-plugin versioning=maven
mvn org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356:sonar
```

In XML files:

```xml
<palantirJavaFormat>
  <!-- renovate: datasource=maven depName=com.palantir.javaformat:palantir-java-format versioning=maven -->
  <version>2.83.0</version>
  <style>GOOGLE</style>
</palantirJavaFormat>
```

For more information, see supported [data sources](https://docs.renovatebot.com/modules/datasource/) and
[versionings](https://docs.renovatebot.com/modules/versioning/).

### Bot Configuration

The Renovate Bot configuration is stored in the build system job, which runs Renovate regularly. It contains the
credentials to provide access to the software repositories and registries, and global configuration options.
The Renovate bot can be configured via [environment variables](https://docs.renovatebot.com/self-hosted-configuration/).

#### GitHub

```yaml
name: Renovate Dependencies

on:
  workflow_dispatch:
    inputs:
      repository_name:
        description: The repository name.
        type: string
        required: false
        default: my-github-org/**
jobs:

  renovate:
    runs-on: ubuntu-latest
    steps:

      - name: Run Renovate Bot
        uses: renovatebot/github-action@v40.3.5
        env:
          # You can find all configuration options here:
          # https://docs.renovatebot.com/self-hosted-configuration
          LOG_LEVEL: ${{ runner.debug == '1' && 'debug' || 'info' }}
          RENOVATE_PLATFORM: github
          # The token needs to be able to create pull requests
          # in the target repositories
          RENOVATE_TOKEN: ${{ secrets.RENOVATE_TOKEN }}
          RENOVATE_AUTODISCOVER: true
          RENOVATE_AUTODISCOVER_FILTER: ${{ inputs.repository_name }}
          # Only run on repositories tagged with 'renovate'
          RENOVATE_AUTODISCOVER_TOPICS: renovate
          # Credentials to access a private registry
          RENOVATE_HOST_RULES: |
            [
              {
                "hostType": "maven",
                "matchHost": "artifactory.example.com",
                "username": "${{ vars.ARTIFACTORY_USERNAME }}",
                "password": "${{ secrets.ARTIFACTORY_TOKEN }}"
              }
            ]
          RENOVATE_PACKAGE_RULES: |
            [
              {
                "matchManagers": ["maven"],
                "matchPackageNames": ["com.example**"],
                "registryUrls": ["https://artifactory.example.com/maven"]
              }
            ]
```

#### Bitbucket

```bash
LOG_LEVEL="info"
RENOVATE_PLATFORM="bitbucket-server"
RENOVATE_ENDPOINT="https://example.com/stash"
RENOVATE_GIT_FS="http"
RENOVATE_USERNAME="<BITBUCKET-USERNAME>"
RENOVATE_PASSWORD="<BITBUCKET-PASSWORD>"
RENOVATE_AUTODISCOVER=true
RENOVATE_AUTODISCOVER_FILTER="MYPROJECT/my-repository"
RENOVATE_HOST_RULES='[{"hostType":"maven","matchHost":"artifactory.example.com","username":"<ARTIFACTORY-USERNAME>","password":"<ARTIFACTORY-PASSWORD>"}]'
RENOVATE_PACKAGE_RULES='<SEE GITHUB SETUP>'
GITHUB_COM_TOKEN="<ACCESS-TOKEN-TO-READ-CHANGELOGS-FROM-GITHUB>"
```
