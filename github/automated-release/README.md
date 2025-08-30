# Automated Releases

Releasing a software project conventionally involves a lot of manual work and is prone to errors. Fortunately, there are
tools that allow you to automate this process:

* You need to decide what version to release under
* You need to adjust the version in the project manifest
* You need to create the changelog for the new version
* You need to create a tag for the new version
* You need to build the software artifact

## Release Please

[Release Please](https://github.com/googleapis/release-please) is a tool that manages a draft release pull request that
is constantly updated whenever a change is merged into the main branch. In this pull request, you can always see which
version the next release will have and how the changelog will look like. To create the release, you only need to merge
the pull request. After the merge, it creates a tag and a GitHub Release for the new version, and your build workflow
builds and publishes the software artifact.

<img alt="Release Pull Request" width="500px" src="https://github.com/googleapis/release-please/raw/main/screen.png">

<https://github.com/googleapis/release-please>

Release Please automatically calculates the next release version and the changelog from the commit messages.
It requires your commit messages to adhere to the [Conventional Commits](https://www.conventionalcommits.org/) format:

```text
feat(ui): add Button component
```

### Setup

To set it up, you need to create GitHub workflow that runs the [release-please-action](https://github.com/googleapis/release-please-action).
You can configure it through the [release-please-config.json](https://github.com/googleapis/release-please/blob/main/docs/manifest-releaser.md)
file.

<details>

#### .github/workflows/build-release.yaml

```yaml
name: Build Release

on:
  push:
    branches:
      - main

permissions:
  contents: write       # This is required for creating the tag
  pull-requests: write  # This is required for creating the pr

jobs:
  prepare-release:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout repo
        uses: actions/checkout@v4
      - name: Run release-please
        id: prepare-release
        uses: googleapis/release-please-action@v4
        with:
          config-file: release-please-config.json
          manifest-file: .release-please-manifest.json
          token: ${{ secrets.GITHUB_TOKEN }}
    outputs:
      release_created: ${{ steps.prepare-release.outputs.release_created }}

  build:
    needs: [ prepare-release ]
    if: needs.prepare-release.outputs.release_created == 'true'
    uses: ./.github/workflows/build.yaml
    with:
      build_release: true
      push_package: true
```

#### release-please-config.json

```json
{
  "$schema": "https://raw.githubusercontent.com/googleapis/release-please/main/schemas/config.json",
  "packages": {
    ".": {
      "release-type": "simple"
    }
  },
  "draft-pull-request": true,
  "plugins": ["sentence-case"],
  "extra-files": ["README.md"],
  "changelog-sections": [
    { "type": "feat", "section": "🚀 New Features"},
    { "type": "fix", "section": "🐞 Bug Fixes" },
    { "type": "docs", "section": "📚 Documentation" },
    { "type": "perf", "section": "⚙️ Technical Enhancements" },
    { "type": "refactor", "section": "⚙️ Technical Enhancements" }
  ]
}
```

</details>

## Other Tools

### semantic-release

[semantic-release](https://github.com/semantic-release/semantic-release) is a npm-based tool that can determine the next
release version and the changelog from conventional commit messages.
With many available [plugins](https://github.com/semantic-release/semantic-release/blob/master/docs/extending/plugins-list.md),
it offers great flexibility. It has similar capabilities as Release Please and provides more customization options,
though it is a bit harder to set up.

### release-drafter

[release-drafter](https://github.com/release-drafter/release-drafter) is a GitHub action that drafts your next release
notes as pull requests are merged into the main branch. It can determine the next release version based on the labels of
the merged pull requests and can automatically create a draft release with the release notes on GitHub.
However, it can neither update the version in the project manifest nor create a tag or save the changelog in the
repository.

You need to build the rest of the release flow manually using additional actions:

* [stefanzweifel/changelog-updater-action](https://github.com/stefanzweifel/changelog-updater-action)
* [peter-evans/create-pull-request](https://github.com/peter-evans/create-pull-request)
