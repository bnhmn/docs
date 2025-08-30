# Revert a Squashed Commit

When [squash merging](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/incorporating-changes-from-a-pull-request/about-pull-request-merges#squash-and-merge-your-commits)
is enabled for your repository, and you merge a pull request, GitHub combines all commits from the pull request into a
single squash commit and appends it to the main branch.
Advantages: Linear history, no trash commits on the main branch, better overview over the history.

If necessary, you can still revert one of your original commits later, as GitHub retains all the original commits of the
pull requests.

1. Jump to the pull request on GitHub and copy the pull request number (e.g. **#36**) and the commit sha of the commit
   you want to revert.
2. Run `git fetch origin pull/36/head:pull/36` to fetch the pull request to your local machine.
3. Run `git revert <commit-sha>` to revert the commit.
