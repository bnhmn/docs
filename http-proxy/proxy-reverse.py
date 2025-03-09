import json
import xml.dom.minidom
from pprint import pformat

from mitmproxy import http


def request(flow: http.HTTPFlow):
    # Redirect all incoming requests to another host using Reverse Proxy mode
    # https://docs.mitmproxy.org/stable/concepts-modes/#reverse-proxy
    flow.request.host = "my-target-host.com"
    flow.request.scheme = "https"
    flow.request.port = 443

    headers = pretty_print_headers(flow.request)
    body = pretty_print_body(flow.request)
    with open("http_traffic.log", "a") as log:
        log.write("\n======== Request ========\n")
        log.write(flow.request.method + " " + flow.request.url + "\n")
        log.write("\n--- Headers ---\n")
        log.write(headers + "\n")
        log.write("\n--- Body ---\n")
        log.write(body + "\n")


def response(flow: http.HTTPFlow):
    headers = pretty_print_headers(flow.response)
    body = pretty_print_body(flow.response)
    with open("http_traffic.log", "a") as log:
        log.write("\n======== Response ========\n")
        log.write("HTTP/" + str(flow.response.http_version) + " " + str(flow.response.status_code) + "\n")
        log.write("\n--- Headers ---\n")
        log.write(headers + "\n")
        log.write("\n--- Body ---\n")
        log.write(body + "\n")


def pretty_print_headers(requestOrResponse: http.Request):
    return json.dumps(dict(requestOrResponse.headers), indent=4)


def pretty_print_body(requestOrResponse: http.Request):
    content_type = requestOrResponse.headers.get("Content-Type", "")
    content = requestOrResponse.get_text(strict=False)
    if "application/json" in content_type:
        return json.dumps(json.loads(content), indent=4)
    if "application/xml" in content_type or "text/xml" in content_type:
        parsed_xml = xml.dom.minidom.parseString(content)
        pretty_xml_as_string = parsed_xml.toprettyxml()
        return "\n".join([line for line in pretty_xml_as_string.split("\n") if line.strip()])
    return content
