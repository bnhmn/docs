# Docker Compose

[Docker Compose](https://docs.docker.com/compose/) is a tool for managing multi-container applications.
It uses a YAML file to configure the application's services, networks, and volumes, allowing you to start all containers
with a single `docker compose up` command.
It simplifies the setup and orchestration of complex applications that require multiple services to work together.

## Docker Compose File

This example `docker-compose.yaml` file starts an [nginx](https://nginx.org/) web server that is accessible on
port 8080 of the local machine.

```yaml
services:
  nginx:
    image: nginx:latest
    ports:
      - "8080:80"
    volumes:
      - ./html:/usr/share/nginx/html
```

For more details on how these files work, see [Docker Compose Application Model](https://docs.docker.com/compose/intro/compose-application-model/).

## Run in the background

```bash
docker compose up -d
```

## Restart all containers

```bash
docker compose down && docker compose up -d
```

## Delete containers and volumes

```bash
docker compose down -v
```

## Attach to a running service

```bash
docker compose exec -it nginx bash
```

## Start a service with a custom command

```bash
docker compose run nginx bash
```
