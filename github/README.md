# GitHub

[GitHub](https://github.com/) is a web-based service built on [Git](../git/) that allows software developers to
collaborate on projects, providing a space to store code, track changes and manage versions.
Beyond version control, GitHub includes features such as issue tracking and continuous integration and deployment.
It's popular for hosting open-source projects, because it's **free for non-commercial use** and has a large community
of developers contributing to open-source projects.

## Guides

* [automated-release](./automated-release/) - Automate the release process.
* [code-owners](./code-owners/) - Set up a code owners file.
* [dependency-updates](./dependency-updates/) - Set up automatic dependency updates.
* [git-blame-ignore](./git-blame-ignore/) - Ignore certain commits in the git blame view.
* [monorepo-build](./monorepo-build/) - Manage and build multiple modules in a single GitHub repository.
* [push-from-actions](./push-from-actions/) - Push code changes from GitHub Actions.
* [release-notes-by-label](./release-notes-by-label/) - Categorize release notes by pull request label.
* [revert-squashed-commit](./revert-squashed-commit/) - Revert a commit that was squash merged.

## Migrate a Git Repository to GitHub

Create a new repository on GitHub. Open a terminal in your local Git repository folder and execute these commands to
push all commits and tags to the new GitHub repository:

```bash
git remote set-url origin <NEW-REPO-URL>
git push -u origin master:main --tags
git checkout -b main origin/main
```

## Useful Repository Settings

```text
Collaborators and teams - Grant read and write permissions to the repository

General -> Allow squash merging -> Pull request title and description
General -> Allow auto merge
General -> Automatically delete head branches

Rules -> Rulesets -> Add branch protection rule
  Restrict deletions
  Block force pushes
  Require a pull request before merging
  Require status checks to pass

Autolink references - Auto link to ticket in issue tracking tool (e.g. Jira)
```
