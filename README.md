# Base gitconfig

This project contains a simple .gitconfig file containing basic configuration options: 

## Explanation

`credential.helper = store`

Saves all repository credentials in clear text to the file `~/.git-credentials`. \
You don't need to type in the password on every push / pull anymore.

`core.autocrlf = true`

Automatically converts Windows line endings `\r\n` to Unix line endings `\n` on push and vice versa on pull. \
On Unix systems maybe better set this option to `input`. 
More information [here](https://stackoverflow.com/questions/3206843/how-line-ending-conversions-work-with-git-core-autocrlf-between-different-operat).

`pull.rebase = true`

On each pull uses `rebase` instead of default `merge` to combine remote changes with local changes. 
This avoids having many superfluous merge commits when working together on a feature branch. \
Rebase re-formulates your local changes to be relative to the newer version on the remote. \
Read more about that [here](https://coderwall.com/p/7aymfa/please-oh-please-use-git-pull-rebase) and [here](https://stackoverflow.com/a/4675513/6316545).

`fetch.prune = true` 

On each fetch or pull, automatically removes git objects like branches that were removed on the remote but still existing locally.

`diff.colorMoved = zebra`

Displays moved text passages in another color instead of the red and green color for deleted and added lines in the diff viewer.

`branch.autoSetupMerge = always` \
`push.default = current`

When checking out a branch, automatically links to the corresponding remote branch so that you can type
`git push` and `git pull` without specifying the branch name all the time.
Read more [here](https://stackoverflow.com/a/41327622/6316545).

`pager.branch = cat`

Uses cat as the pager for the `git branch` command. On most git distributions this is already the default but it wasn't on my Mac.
