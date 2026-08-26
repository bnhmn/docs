# cURL

[cURL](https://curl.se/) is a command-line tool and library for fetching and transferring data from URLs.
It is commonly used for making requests to web services, downloading files, and testing APIs. It is widely
used for scripting because of its flexibility and ease of use.

## Testing Connectivity

This command suppresses the response body and prints only the HTTP status code. It is useful for quick connectivity
and health checks in scripts to verify if a server is reachable and if your credentials are valid:

```bash
curl -s -o /dev/null -w "%{http_code}\n" https://example.com
```

```bash
curl -s -o /dev/null -w "%{http_code}\n" -u "$NEXUS_USERNAME:$NEXUS_PASSWORD" https://nexusrepository.example.com/repository/maven-public/
```

## Safe Curl Command

A curl command with default configuration:

* Fail on status code greater or equal to 400 and print response body to stderr
* Follow http redirects
* Retry on timeouts, server errors and too many requests (429)

NOTE: In case of retries, the stdout response contains multiple response bodies.

```bash
#!/usr/bin/env bash

set -o errexit -o nounset -o pipefail

safe_curl() {
  response=$(
    curl --silent --show-error --fail-with-body --location --retry 2 "${@}"
  ) || { echo "$response" >&2; exit 1; }
  echo "$response"
}
```

Sample usage:

```bash
safe_curl \
--request "POST" "https://example.com/api" \
--header "Authorization: Bearer ${AUTH_TOKEN}" \
--header "Content-Type: application/json" \
--data '{"hello": "world"}'
```
