# Git

[Git](https://git-scm.com/) is a distributed version control system that allows developers to track changes in source
code during software development. It enables multiple users to collaborate on projects, manage code versions, and revert
to previous states if needed, all while maintaining a complete history of code changes. Git is widely used for its
efficiency, flexibility, and support for branching and merging.

## Git Tutorial

See <https://git-scm.com/docs/gittutorial>.

## Git Commands

```text
Accept my version of a file        git checkout --ours -- <filename>
Accept their version of a file     git checkout --theirs -- <filename>
```

## Git Tooling

* [git-cliff](https://git-cliff.org/docs/) - Generate a changelog file from the Git history.

## Git Config

Git configuration files contains settings that influence the Git commands' behavior.

* The file `.git/config` in a repository is used to store the configuration for that repository.
* The file `$HOME/.gitconfig` is used to store a per-user default configuration.
* The file `/etc/gitconfig` can be used to store a system-wide default configuration.

See [git config](https://git-scm.com/docs/git-config#_configuration_file) for explanations and available options.

This is a sample user configuration file:

```ini
[user]
  name = John Doe
[core]
  autocrlf = input  # Convert Windows line endings to Unix line endings
[fetch]
  prune = true      # Remove outdated tags and branches on git fetch
[color]
  ui = auto
[branch]
  autoSetupMerge = always
[push]
  default = current # Auto link current branch to corresponding remote branch
                    # (make git push and git pull work without args)
[pager]
  branch = cat      # Use cat as the pager for the git branch command
                    # (on macOS the default is less)
```

### Multi Tenant Setup

If you work for different companies or simply want to separate business projects from personal ones, you can create
separate directories for each project and set up individual Git configurations for each one.

Create the following folder structure in your home directory:

```text
git
├── PROJECT1
│   └── .gitconfig
└── PROJECT2
    └── .gitconfig
```

Extend your Git user configuration file at `~/.gitconfig`:

```ini
[includeIf "gitdir:~/git/PROJECT1/"]
  # Include this config if git repo is inside PROJECT1 folder
  path = ~/git/PROJECT1/.gitconfig

[includeIf "gitdir:~/git/PROJECT2/"]
  # Include this config if git repo is inside PROJECT2 folder
  path = ~/git/PROJECT2/.gitconfig
```

Create the `PROJECT1/.gitconfig` file

```ini
[user]
  email = john.doe@company-a.com
[credential "https://github.com"]
  username = john-doe-company-a
```

Create the `PROJECT2/.gitconfig` file

```ini
[user]
  email = john.doe@company-b.com
[credential "https://github.com"]
  username = john-doe-company-b
```

When you now switch to the `PROJECT1` folder and clone a GitHub repository, Git will use the email and credentials
for **company a**.
Similarly, for all Git repositories in the `PROJECT2` folder, Git will use the email and credentials for **company b**.
