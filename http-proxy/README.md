# MITM HTTP proxy

mitmproxy is a powerful Python-based HTTP proxy that can be used to intercept and log HTTP requests.

- <https://docs.mitmproxy.org/stable/concepts-howmitmproxyworks/>
- <https://docs.mitmproxy.org/stable/concepts-modes/>.

## How to Run

Run `pip install -r requirements-txt`

Run `mitmdump -s proxy.py -p 9090`

The proxy will listen for incoming requests on `http://localhost:9090`.
