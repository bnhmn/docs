# Docker

[Docker](https://www.docker.com/) is a popular tool that helps package applications into self-contained units called Containers.

These Containers include all the dependencies required by the application.

## Dockerfile

A **Dockerfile** is a set of instructions that tells Docker how to build a **Docker Image**.

For instance, consider this example Dockerfile used to build an Image for a Java application:

```Dockerfile
FROM ibm-semeru-runtimes:open-17-jre

WORKDIR /app
COPY ./java-app.jar ./java-app.jar

CMD [ "java", "-jar", "java-app.jar" ]
```

<!-- markdownlint-disable MD033 -->
<details>
<summary>Instructions commonly used</summary>

* **FROM** - Set the base Image (Images can be found in the [Docker Hub](https://hub.docker.com/))
* **RUN** - Run shell commands
* **WORKDIR** - Set the working directory
* **COPY** - Copy local files into the Image
* **ENV** - Set environment variables
* **CMD** - Set the start command

</details>

## Build an Image

To build an Image, open a terminal inside the directory containing the Dockerfile and execute the following command:

```bash
docker build .
```

<details>
<summary>Tags are used to identify an Image</summary>

Docker executes the instructions from the Dockerfile in sequence and creates the Image accordingly.
The resulting Image is identified by a unique SHA256 hash.

To upload an Image to a repository, it's required to assign it a **Tag**.
A Tag is an alternative name for the Image and typically follows the pattern `<name>:<version>`.

</details>

To build the Image with the specific Tag `java-app:1.0.0`, use the following command:

```bash
docker build -t java-app:1.0.0 .
```

## Execute an Image

To run an Image, which essentially means starting a Container, use the following command:

```bash
docker run -it java-app:1.0.0
```

<details>
<summary>Cleanup, environment variables, file mounting, custom commands, background mode</summary>

Delete the Container after it shuts down using the `--rm` option:

```bash
docker run --rm -it java-app:1.0.0
```

Pass environment variables using the `-e` option followed by the variable name and its value:

```bash
docker run -e USERNAME=admin -e PASSWORD=12345678 -it java-app:1.0.0
```

Mount a local directory or file into the Container using the `-v` option followed by the absolute file paths:

```bash
docker run -v /tmp:/tmp -it java-app:1.0.0
```

To execute a custom command (e.g. `bash`) within the Image, specify the command after the Image name:

```bash
docker run -it java-app:1.0.0 bash -c "echo Hello World!"
```

Run the Image in the background (aka detached mode) using the `-d` option:

```bash
docker run -d java-app:1.0.0
```

</details>

## Attach to a Container

<details>
<summary>Start a named Container in the background</summary>

---

When you run a Container in the background using

```bash
docker run -d java-app:1.0.0
```

the command will output the Container's ID. You can use this ID to attach to the running Container later.

Alternatively, you can give the Container a specific name during startup, making it easier to reference:

```bash
docker run --name my-container -d java-app:1.0.0
```

---
</details>

Attach to a running Container using

```bash
docker attach my-container
```

Execute a shell in a running Container

```bash
docker exec -it my-container bash
```

## Stop a Container

```bash
docker stop my-container
```
