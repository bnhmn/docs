# Renovate

<img width="80px" src="https://docs.renovatebot.com/assets/images/logo.png" alt="Renovate Logo">

You can use [Renovate](https://docs.renovatebot.com/) to automatically update software dependencies.
Renovate is an automated tool that scans software repositories for dependencies, checks whether newer versions of those dependencies are available, and then creates pull requests with these updates in the project's codebase.
Renovate needs to be set up and configured one-time for each repository. During this process, a job must be created in the build system which will run Renovate at regular intervals.

## Execution

Renovate can be executed either via [Node](https://nodejs.org/en) package manager or [Docker](https://docs.docker.com/get-docker/)

```bash
npx renovate
```

```bash
docker run renovate/renovate
```

## Configuration

Renovate needs to be configured at two levels: The **Renovate bot configuration** and the **repository configuration**.

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

### Repository config

The repository configuration is located in the software repository, whose dependencies are being tracked,
and it solely concerns this repository. There, for example, the granularity of the pull requests can be adjusted.
The repository can be configured via a [renovate.json](https://docs.renovatebot.com/configuration-options/) file:

```json
{
  "$schema": "https://docs.renovatebot.com/renovate-schema.json",
  "description": "See https://docs.renovatebot.com/configuration-options for available options",
  "extends": [
    "github>my-github-org/actions//renovate/default"
  ],
  "ignorePaths": [
    ".github/workflows/**"
  ]
}
```

File `renovate/default.json` in repository `my-github-org/actions`:

```json
{
  "$schema": "https://docs.renovatebot.com/renovate-schema.json",
  "description": "See https://docs.renovatebot.com/configuration-options for available options",
  "extends": [
    "config:best-practices",
    ":disableDependencyDashboard",
    "mergeConfidence:all-badges"
  ],
  "minimumReleaseAge": "7 days",
  "internalChecksFilter": "strict",
  "packageRules": [
  ]
}
```
