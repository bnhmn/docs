# Slack

[Slack](https://slack.com/) is a digital communication platform designed for team collaboration. It provides features
like group conversations through channels, direct messaging between users, file sharing, and the ability to integrate
other work tools. Its goal is to streamline communication within teams and reduce reliance on email.

## Webhooks

You can send messages to a Slack channel via its [WebHook URL](https://api.slack.com/messaging/webhooks).
This URL is like a password, you should handle it with care!

You can send simple plain text messages as seen here:

```http
POST https://hooks.slack.com/services/T00000000/B00000000/XXXXXXXXXXXXXXXXXXXXXXXX
```

```json
{
  "text": "Hello!"
}
```

or you can choose from many advanced [UI blocks](https://api.slack.com/block-kit/building) to create more complex
messages. Try it out in the [Block Kit Builder](https://app.slack.com/block-kit-builder)!

### Block Kit Example

<img src="./error-notification.png" width="450px" alt="Error Notification">

```json
{
  "blocks": [
    {
      "type": "section",
      "text": {
        "type": "mrkdwn",
        "text": "*internal-server-error*\n\nThe connection was prematurely closed.\n<https://logs.example.com/logs/1234567890|See logs>"
      }
    },
    {
      "type": "context",
      "elements": [
        {
          "type": "plain_text",
          "text": "Environment: local"
        },
        {
          "type": "plain_text",
          "text": "Client ID: test-client"
        },
        {
          "type": "plain_text",
          "text": "URI: /transactions"
        }
      ]
    }
  ]
}
```
