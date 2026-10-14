# Grafana Local Setup

This folder contains a [docker-compose.yml](./docker-compose.yml) file to quickly spin up a Grafana instance locally.

Run `docker compose up` to start the following services:

- Grafana on <http://localhost:3000/> (username: `admin`, password: `admin`)
- Prometheus on <http://localhost:9090/>

## Prometheus Configuration

Prometheus is configured in file [prometheus.yml](./prometheus.yml).
It is setup to scrape metrics from:

- **prometheus**: The Prometheus instance itself running inside the Docker container.
- **spring-boot**: A Spring Boot application running on the host machine outside of Docker.

## Grafana Configuration

The connection from Grafana to Prometheus is configured in file [grafana-datasource.yml](./grafana-datasource.yml).
