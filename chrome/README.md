# Chrome

[Google Chrome](https://www.google.com/intl/en-us/chrome/) is a web browser developed by Google, known for its
simplicity and support for extensions, available on various platforms.

## Run Chrome without CORS

[CORS (Cross-Origin Resource Sharing)](https://developer.mozilla.org/de/docs/Web/HTTP/Guides/CORS) is a web browser
security feature that allows or restricts web pages from making requests to different domains, using HTTP headers to
control access and protect user data.

Run Chrome with disabled CORS using the following command:

```bash
"/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" --disable-web-security --user-data-dir="/tmp/chrome-nocors"
```
