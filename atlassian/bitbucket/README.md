# Bitbucket

Bitbucket is Atlassian's Git repository hosting and collaboration platform. It supports source-code management,
pull requests, code reviews, and CI/CD integrations.

## Code Owners

If you want to set up [code owners](https://confluence.atlassian.com/bitbucketserver/code-owners-1296171116.html),
create a `.bitbucket/CODEOWNERS` file in your Bitbucket repository. Each line contains a path pattern followed by
one or more Bitbucket usernames or email addresses:

```text
# Code owners are automatically added as pull request reviewers.
# See https://confluence.atlassian.com/bitbucketserver/code-owners-1296171116.html

# The following rule matches all changes in the repository, so unless a rule
# lower in the file matches, globalowner will always be suggested as a code owner
** @globalowner

# The following patterns will match every file in the frontend/backend directory and all subdirectories
frontend/** @frontend-team
backend/** @backend-team
.bitbucket/** @admin123 john.doe@example.com
```

## Repository Setup

If you are about to create a new Bitbucket repository or reconfigure an existing one, this provides a recommended
minimal configuration to enable effective collaboration within a team (branch protection, mandatory
code review, etc.):

```text
Repository details -> Default branch -> master

Branch permissions (default branch)
  -> Prevent Rewriting history
  -> Prevent Deletion
  -> Prevent Changes without a pull request

Branches -> (scroll to bottom) -> Branch deletion on merge -> On

Merge checks
  -> Minimum approvals -> Enable -> Number of approvals: 1
  -> Minimum successful builds -> Enable -> Number of builds: 1
  -> No 'changes requested' state -> Enable
  -> No incomplete tasks -> Enable

Merge strategies
  -> Squash -> Enable -> Set as default
  -> Disable all other merge strategies

Auto-merge -> Enable auto-merge
```

## REST API

You can find the full documentation for the Bitbucket Data Center REST API here:
<https://developer.atlassian.com/server/bitbucket/rest/v1004/intro/#about>.

Note that you may need to adjust the URL to match your Bitbucket version (see [Bitbucket DC Changelog](https://developer.atlassian.com/server/bitbucket/reference/api-changelog/)).

### Authentication

The REST API supports multiple authentication methods. Basic authentication is the simplest option, but some
installations may require the HTTP access token method. Add one of the following options to your `curl` command:

#### Basic authentication with username and password

```bash
BITBUCKET_BASE_URL="https://bitbucket.example.com/bitbucket/rest/api"
BITBUCKET_USERNAME="<your-bitbucket-username>"
BITBUCKET_PASSWORD="<your-bitbucket-password>"
curl --user "$BITBUCKET_USERNAME:$BITBUCKET_PASSWORD"
```

#### HTTP access token in Authorization bearer header

```bash
BITBUCKET_BASE_URL="https://bitbucket.example.com/bitbucket/rest/api"
BITBUCKET_TOKEN="<some-personal-access-token>"
curl --header "Authorization: Bearer $BITBUCKET_TOKEN"
```

### [List users](https://developer.atlassian.com/server/bitbucket/rest/v1004/api-group-system-maintenance/#api-api-latest-users-get)

```bash
curl "$BITBUCKET_BASE_URL/api/latest/users?filter=jdoe" \
  --header "Authorization: Bearer $BITBUCKET_TOKEN" \
  --header "Content-Type: application/json" | jq
```

```json
{
  "size": 1,
  "limit": 25,
  "isLastPage": true,
  "values": [
    {
      "name": "jdoe",
      "emailAddress": "john.doe@example.com",
      "active": true,
      "displayName": "Jane Doe",
      "id": 123456,
      "slug": "jdoe",
      "type": "NORMAL"
    }
  ],
  "start": 0
}
```
