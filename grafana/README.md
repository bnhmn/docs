# Grafana

[Grafana](https://grafana.com/) is a monitoring tool that allows you to create dashboards to monitor the health
of your systems, services and applications.
A dashboard visualizes metrics of the service, such as CPU usage, memory usage, requests per second, and error counts.

[Prometheus](https://prometheus.io/) is a metrics scraper that pulls metrics from your applications and acts as a data source for Grafana.

This project contains a [docker-compose.yml](./docker-compose.yml) file to quickly spin up a Grafana instance locally.

Run `docker compose up` to start the following services:

- Grafana on `http://localhost:3000/` (username: `admin`, password: `admin`)
- Prometheus on `http://localhost:9090/`

## Prometheus Configuration

Prometheus is configured in file [prometheus.yml](./prometheus.yml).
It is setup to scrape metrics from:

- **prometheus**: The Prometheus instance itself running inside the Docker container.
- **spring-boot**: A Spring Boot application running on the host machine outside of Docker.
