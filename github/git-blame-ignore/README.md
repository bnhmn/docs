# Git Blame Ignore

You can create a git blame ignore file to make GitHub ignore certain commits in the blame view. This is particularly
useful, for example, if you have recently run a code formatter over the entire repository but still want the original
author to be displayed in git blame. The file name is usually `.git-blame-ignore-revs` and the format is described
in [git-fsck's man](https://git-scm.com/docs/git-fsck#Documentation/git-fsck.txt-fsckskipList).

GitHub supports this natively: [Ignore commits in the blame view](https://docs.github.com/en/repositories/working-with-files/using-files/viewing-and-understanding-files#ignore-commits-in-the-blame-view).

To use it locally with `git blame`, you need to execute this command to enable it:

```bash
git config blame.ignoreRevsFile .git-blame-ignore-revs
```

## Example

```text
# .git-blame-ignore-revs
# Removed semi-colons from the entire codebase
a8940f7fbddf7fad9d7d50014d4e8d46baf30592
# Converted all JavaScript to TypeScript
69d029cec8337c616552756310748c4a507bd75a
```
