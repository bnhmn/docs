# Confluence REST API

## Reference

If your Confluence server is on version <= 8.5 LTS, see <https://developer.atlassian.com/cloud/confluence/rest/v1>. \
If your Confluence server is on a newer version, see <https://developer.atlassian.com/cloud/confluence/rest/v2>.

## Examples

### [Get All Pages](https://developer.atlassian.com/cloud/confluence/rest/v1/api-group-content/#api-wiki-rest-api-content-get)

```bash
CONFLUENCE_BASE_URL="https://example.com/confluence/rest/api"
CONFLUENCE_SPACE_KEY="MYPROJECT"
CONFLUENCE_AUTH_TOKEN="<some-personal-access-token>"

curl \
  --request "GET" "$CONFLUENCE_BASE_URL/content?type=page&spaceKey=$CONFLUENCE_SPACE_KEY" \
  --header "Authorization: Bearer $CONFLUENCE_AUTH_TOKEN" \
  --header "Content-Type: application/json" | jq
```

### [Get Page by Title with Body](https://developer.atlassian.com/cloud/confluence/rest/v1/api-group-content/#api-wiki-rest-api-content-get)

```bash
curl \
  --request "GET" "$CONFLUENCE_BASE_URL/content?type=page&spaceKey=$CONFLUENCE_SPACE_KEY&title=ABC&expand=version,body.storage" \
  --header "Authorization: Bearer $CONFLUENCE_AUTH_TOKEN" \
  --header "Content-Type: application/json" | jq
```
