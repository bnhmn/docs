# Automated Releases

Releasing a software project conventionally involves a lot of manual work and is prone to errors. Fortunately, there are
tools that allow you to automate this process:

* You need to decide what version to release under
* You need to adjust the version in the project manifest
* You need to create the changelog for the new version
* You need to create a tag for the new version
* You need to build the software artifact

## Tools

### release-please

[Release Please](https://github.com/googleapis/release-please) is a tool that manages a draft release pull request that
is constantly updated whenever a change is merged into the main branch. In this pull request, you can always see which
version the next release will have and how the changelog will look like. It determines the next release version and the
changelog from [conventional commit](https://www.conventionalcommits.org/) messages. To create the release, you only need
to merge the pull request. After the merge, it creates a tag and a GitHub Release for the new version, and your build
workflow builds and publishes the software artifact.

<details>

<img alt="Release Pull Request" width="500px" src="https://github.com/googleapis/release-please/raw/main/screen.png">

<https://github.com/googleapis/release-please>

Release Please automatically calculates the next release version and the changelog from the commit messages.
This requires your commit messages to follow the [Conventional Commits](https://www.conventionalcommits.org/) format:

```text
feat(ui): add Button component
```

### Setup

To set it up, you need to create a GitHub workflow that runs the [release-please-action](https://github.com/googleapis/release-please-action).
You can configure it through the [release-please-config.json](https://github.com/googleapis/release-please/blob/main/docs/manifest-releaser.md)
file.

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

### semantic-release

[semantic-release](https://github.com/semantic-release/semantic-release) is a npm-based tool that can determine the next
release version and the changelog from conventional commit messages.
With many available [plugins](https://github.com/semantic-release/semantic-release/blob/master/docs/extending/plugins-list.md),
it offers great flexibility. It has similar capabilities as Release Please and provides more customization options,
though it is a bit harder to set up.

### git-cliff

[git-cliff](https://github.com/orhun/git-cliff) and the [git-cliff-action](https://github.com/orhun/git-cliff-action)
can generate a changelog from conventional commit messages as well as regex-powered custom parsers. The changelog template
can be customized with a configuration file to match the desired format. See these [examples](https://git-cliff.org/docs/templating/examples).

<details>

```toml
# cliff.toml
# git-cliff configuration file
# https://git-cliff.org/docs/configuration

[changelog]
# A Tera template to be rendered as the changelog's header.
# See https://keats.github.io/tera/docs/#introduction
header = """
# Changelog\n
All notable changes to this project will be documented in this file.
\n---\n
"""
# A Tera template to be rendered for each release in the changelog.
# See https://keats.github.io/tera/docs/#introduction
body = """
{% macro print_commit(commit) -%}
    - {% if commit.scope %}<ins>{{ commit.scope }}</ins> - {% endif %}{{ commit.message | upper_first }}\
{% endmacro -%}

{% if version %}\
    ## [{{ version }}](<REPO>/releases/tag/{{ version }}) - {{ timestamp | date(format="%Y-%m-%d") }}
{% else %}\
    ## [unreleased]
{% endif %}\

{% for group, commits in commits | group_by(attribute="group") %}
    ### {{ group | striptags | trim | upper_first }}
    {% for commit in commits
    | filter(attribute="scope")
    | sort(attribute="scope") %}
        {{ self::print_commit(commit=commit) }}
    {%- endfor %}
    {% for commit in commits %}
        {%- if not commit.scope -%}
            {{ self::print_commit(commit=commit) }}
        {% endif -%}
    {% endfor -%}
{% endfor -%}

{% if previous.version %}
---\n
{% endif %}
"""
# Remove leading and trailing whitespaces from the changelog's body.
trim = true
# An array of regex based postprocessors to modify the changelog.
postprocessors = [
  # Replace the placeholders with a URL.
  { pattern = '<REPO>', replace = "https://github.com/my-github-org/my-repo" },
  { pattern = '<JIRA>', replace = "https://example.com/jira/browse" },
]

[git]
# Parse commits according to the conventional commits specification.
# See https://www.conventionalcommits.org
conventional_commits = true
# Exclude commits that do not match the conventional commits specification.
filter_unconventional = true
# An array of regex based parsers to modify commit messages prior to further processing.
commit_preprocessors = [
  # Replace issue numbers with link templates to be updated in `changelog.postprocessors`.
  { pattern = '\((\w+\s)?#([0-9]+)\)', replace = "([#${2}](<REPO>/issues/${2}))" },
  { pattern = 'CVE-\d{4}-\d+', replace = "[[${0}](https://www.google.com/search?q=${0})]" },
  { pattern = '\(([A-Z][A-Z]+-\d+)\)', replace = "[[${1}](<JIRA>/${1})]" }
]
# An array of regex based parsers for extracting data from the commit message.
# Assigns commits to groups.
# Optionally sets the commit's scope and can decide to exclude commits from further processing.
commit_parsers = [
  { message = "!: ", group = "<!-- 0 -->💥 BREAKING CHANGES" },
  { message = "^feat", group = "<!-- 1 -->🚀 New Features" },
  { message = "^fix", group = "<!-- 2 -->🐞 Bug Fixes" },
  { message = "^perf", group = "<!-- 3 -->⚡ Performance" },
  { message = "^doc", group = "<!-- 4 -->📚 Documentation" },
  { message = "^chore\\(deps.*\\)", scope = "", group = "<!-- 5 -->🔨 Dependency Upgrades" },
  { message = "^style|^refactor|^test|^ci", group = "<!-- 6 -->⚙️ Technical Enhancements" },
  { message = "^revert", group = "<!-- 7 -->◀️ Revert" },
]
# Exclude commits that are not matched by any commit parser.
filter_commits = true
# Regex to select git tags that represent releases.
tag_pattern = "v?[0-9].*"
```

</details>

### release-drafter

[release-drafter](https://github.com/release-drafter/release-drafter) is a GitHub action that drafts your next release
notes as pull requests are merged into the main branch. It can determine the next release version based on the labels of
the merged pull requests and can automatically create a draft release with the release notes on GitHub.
However, it can neither update the version in the project manifest nor create a tag or save the changelog in the
repository.

### changelog-updater-action

[stefanzweifel/changelog-updater-action](https://github.com/stefanzweifel/changelog-updater-action) is a GitHub action
to update a CHANGELOG.md file with the latest release notes from GitHub.

### create-pull-request

[peter-evans/create-pull-request](https://github.com/peter-evans/create-pull-request) is a GitHub action to create a pull
request for changes you've made to files within your GitHub workflow. The action will create a pull request if one does
not already exist, or update an existing pull request if it does.
