# Load Testing

Load and performance testing is about checking if system can handle a lot of users or tasks at the same time without
slowing down or crashing. It tests how fast the system responds and if it works reliably under load. The goal is to find
and fix any issues that could slow down or break the system when it's used heavily.

<img alt="Load test types at a glance" width="800px" src="https://grafana.com/media/docs/k6-oss/chart-load-test-types-overview.png">

<https://grafana.com/load-testing/types-of-load-testing/>


## Load Testing Tools

Locust and Grafana k6 are popular open-source tools used for designing and executing load tests.

### Locust

[Locust](https://locust.io/) allows you to write test scenarios in simple Python code. It supports running load tests distributed over multiple machines, and can therefore be used to simulate millions of simultaneous users.

```python
from locust import HttpUser, task

class HelloWorldUser(HttpUser):
    @task
    def hello_world(self):
        self.client.get("/hello")
        self.client.get("/world")
```

The swarming process and the system's performance can be monitored from a web UI in real-time.



<img alt="Locust Web UI" width="800px" src="https://docs.locust.io/en/stable/_images/webui-running-statistics-light.png">

<https://docs.locust.io/en/stable/quickstart.html>

### Grafana k6

[Grafana k6](https://github.com/grafana/k6) is a very flexible load testing tool that allows writing tests in
JavaScript. It has a simple CLI, allows setting performance thresholds, and supports JSON output for integration
into CI/CD pipelines.

```js
import http from "k6/http";
import { check, sleep } from "k6";

// Test configuration
export const options = {
  thresholds: {
    // Assert that 99% of requests finish within 3000ms.
    http_req_duration: ["p(99) < 3000"],
  },
  // Ramp the number of virtual users up and down
  stages: [
    { duration: "30s", target: 15 },
    { duration: "1m", target: 15 },
    { duration: "20s", target: 0 },
  ],
};

// Simulated user behavior
export default function () {
  let res = http.get("https://quickpizza.grafana.com");
  // Validate response status
  check(res, { "status was 200": (r) => r.status == 200 });
  sleep(1);
}
```
