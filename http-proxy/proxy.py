import json
import xml.dom.minidom
import html5lib
from pprint import pformat

from mitmproxy import http


def request(flow: http.HTTPFlow):
    # Proxy requests using Regular Proxy mode
    # https://docs.mitmproxy.org/stable/concepts-modes/#regular-proxy

    # Example usage in Python:
    # response = requests.request(method=method, url=url, proxies={"https": "http://localhost:9090"}, verify=False)

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
        parsed_json = json.loads(content)
        pretty_json = json.dumps(parsed_json, indent=4)
        return pretty_json
    if "application/xml" in content_type or "text/xml" in content_type:
        parsed_xml = xml.dom.minidom.parseString(content)
        pretty_xml = parsed_xml.toprettyxml()
        pretty_xml = "\n".join([line for line in pretty_xml.split("\n") if line.strip()])
        return pretty_xml
    if "application/html" in content_type or "text/html" in content_type:
        parsed_html = html5lib.parse(content, treebuilder="dom")
        pretty_html = parsed_html.toprettyxml()
        pretty_html = "\n".join([line for line in pretty_html.split("\n") if line.strip()])
        return pretty_html
    return content
