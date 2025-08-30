# Jira REST API

## Reference

Jira 10.0 REST API Docs: <https://developer.atlassian.com/server/jira/platform/rest> \
Jira 9.12 REST API Docs: <https://docs.atlassian.com/software/jira/docs/api/REST/9.12.0/> \
XRAY 1.0 API Docs: <https://docs.getxray.app/display/XRAY/v1.0> \
XRAY 2.0 API Docs: <https://docs.getxray.app/display/XRAY/v2.0> \

## Examples

### Whoami

```bash
JIRA_BASE_URL="https://example.com/jira/rest/api/2"
JIRA_AUTH_TOKEN="<some-personal-access-token>"

curl \
  --request GET "$JIRA_BASE_URL/myself" \
  --header "Authorization: Bearer $JIRA_AUTH_TOKEN"
```

```json
{
  "self": "https://example.com/jira/rest/api/2/user?username=example",
    "key": "JIRAUSER000001",
  "name": "example",
    "emailAddress": "john.doe@example.com",
    "displayName": "John Doe",
    "active": true,
    "deleted": false,
    "timeZone": "Europe/Berlin",
    "locale": "en_US",
    "groups": {
        "size": 12,
        "items": []
    }
}
```
