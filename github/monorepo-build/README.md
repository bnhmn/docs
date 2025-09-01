# Monorepo Build

If you host multiple similar software projects in separate GitHub repositories and they all have the same technical
setup (for example, Java libraries, JavaScript libraries, Helm charts), you can streamline the build and release of
these components by merging them into a [monorepo](https://monorepo.tools/#understanding-monorepos).

There are several tools available for managing monorepos: [monorepo.tools](https://monorepo.tools/#monorepo-tools).

However, instead of using these tools, we're going to show a simple and generic approach here, to host multiple modules
in a GitHub repository. Each module resides in its own subfolder, and the modules are independent of each other:

```text
.
├── module-a
├── module-b
└── module-c
```

The workflows are built in a way that allows you to add a new module just by creating a new folder, without needing to
adjust any configuration.

## Build on PR

On pull request, we only build the modules that were changed. We determine the changed folders using the
[tj-actions/changed-files](https://github.com/tj-actions/changed-files) action.
We then use a matrix job to build the changed modules in parallel.

```yaml
name: Build on PR

on:
  pull_request: { }

permissions:
  contents: read        # This is required to checkout the repo
  pull-requests: read   # This required to detect the changes files

jobs:
  detect-changes:
    runs-on: ubuntu-latest
    steps:
      - name: Detect changed folders
        id: changed-files
        uses: tj-actions/changed-files@ed68ef82c095e0d48ec87eccea555d944a631a4c # v46
        with:
          files: ./module-*/**
          dir_names: true
          dir_names_max_depth: 1
          matrix: true
    outputs:
      any_changed: ${{ steps.changed-files.outputs.any_changed }}
      changed_folders: ${{ steps.changed-files.outputs.all_changed_files }}

  build:
    needs: [ detect-changes ]
    if: needs.detect-changes.outputs.any_changed == 'true'
    uses: ./.github/workflows/build.yaml
    strategy:
      matrix:
        folder: ${{ fromJSON(needs.detect-changes.outputs.changed_folders) }}
      max-parallel: 4
      fail-fast: false
    with:
      folder: ${{ matrix.folder }}
      build_release: false
      push_package: false

  # A marker job that is green when the build was successful or when nothing has changed. Useful as a required check.
  checks-passed:
    needs: [ detect-changes, build ]
    if: always() && needs.detect-changes.outputs.any_changed == 'false' || needs.build.result == 'success'
    runs-on: ubuntu-latest
    steps:
      - run: echo "All checks have passed!"
```

## Build on Main

The build-on-main workflow looks exactly like the build-on-pr workflow, except that it is triggered by a push to the
main branch.

## Build Release

There are different strategies for releasing monorepos. Some people release only the changed modules.
Some release all modules, each under its own version and tag.
For this approach, we'll assume that we always release all modules, regardless of whether they've changed since
the last release or not, and use a common version tag.

```yaml
name: Build Release

on:
  workflow_dispatch: {}

jobs:
  prepare-release:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout repo
        uses: actions/checkout@v4

      - name: Detect modules
        id: detect-modules
        # language=bash
        run: |
          folders=$(find . -maxdepth 1 -type d -name "module-*" -exec basename {} ';')
          folders_json=$(echo "$folders" | jq --raw-input --slurp 'split("\n") | map(select(length > 0))' --compact-output)
          echo "folders=$folders_json" >> "$GITHUB_OUTPUT"

      - name: Create version tag
        run: ...
    outputs:
      folders: ${{ steps.detect-modules.outputs.folders }}

  build:
    needs: [ prepare-release ]
    strategy:
      matrix:
        folder: ${{ fromJSON(needs.prepare-release.outputs.folders) }}
    uses: ./.github/workflows/build.yaml
    with:
      folder: ${{ matrix.folder }}
      build_release: true
      push_package: true
```
