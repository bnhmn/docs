# Docker

[Docker](https://www.docker.com/) is a popular tool that helps package applications into
self-contained units called Containers.

These Containers include all the dependencies required by the application.

## Dockerfile

A **Dockerfile** is a set of instructions that tells Docker how to build a **Docker Image**.

For instance, consider this example Dockerfile used to build an Image for a Java application:

```Dockerfile
# https://github.com/GoogleContainerTools/distroless
FROM gcr.io/distroless/java25-debian13:nonroot

WORKDIR /app
COPY ./java-app.jar ./java-app.jar

CMD [ "java", "-jar", "java-app.jar" ]
```

### Instructions commonly used

- **FROM** - Set the base Image (Images can be found in the [Docker Hub](https://hub.docker.com/))
- **RUN** - Run shell commands
- **WORKDIR** - Set the working directory
- **COPY** - Copy local files into the Image
- **ENV** - Set environment variables
- **CMD** - Set the start command

For a complete list of available options, see
[Dockerfile reference](https://docs.docker.com/reference/dockerfile/).

**Best practices** for writing Dockerfiles for applications written in different languages:
<https://docs.docker.com/language/>.

## Build an Image

To build an Image, open a terminal inside the directory containing the Dockerfile and execute the
following command:

```bash
docker build .
```

<details>
<summary>Tags are used to identify an Image</summary>

Docker executes the instructions from the Dockerfile in sequence and creates the Image accordingly.
The resulting Image is identified by a unique SHA256 hash.

To upload an Image to a repository, it's required to assign it a **Tag**. A Tag is an alternative
name for the Image and typically follows the pattern `<name>:<version>`.

</details>

To build the Image with the specific Tag `java-app:1.0.0`, use the following command:

```bash
docker build -t java-app:1.0.0 .
```

For debugging purposes, it can be useful to switch to plain logging mode, since in standard mode,
build logs are visible in the terminal only while the build is running:

```bash
docker build -t java-app:1.0.0 . --progress=plain
```

## Execute an Image

To run an Image, which essentially means starting a Container, use the following command:

```bash
docker run --rm -it java-app:1.0.0
```

I usually recommend setting the `--rm` option, as it ensures the container is deleted after it shuts
down. If you want to keep a container for longer, simply omit the option.

#### Environment variables

Pass environment variables using the `-e` option followed by the variable name and its value:

```bash
docker run --rm -e USERNAME=admin -e PASSWORD=12345678 -it java-app:1.0.0
```

#### Mount files

Mount a local directory or file into the Container using the `-v` option followed by the absolute
file paths:

```bash
docker run --rm -v /tmp:/tmp -it java-app:1.0.0
```

#### Background mode

Run the Image in the background (aka detached mode) using the `-d` option:

```bash
docker run --rm -d java-app:1.0.0
```

## Debug an Image

To debug file system and permission issues in a container, it can be helpful to start a shell inside
the container:

```bash
docker run --rm -it --entrypoint bash java-app:1.0.0
```

In the shell, you can execute arbitrary commands and explore the file system.

For instance, you can check the user's permissions within the container using these commands:

```bash
whoami
id
ls -lah .
```

You can also search for files if you aren't sure where specific files are located within the
container:

```bash
find / -type f -name '*.crt' 2>/dev/null
```

## Attach to a Container

<details>
<summary>Start a named Container in the background</summary>

---

When you run a Container in the background using

```bash
docker run -d java-app:1.0.0
```

the command will output the Container's ID. You can use this ID to attach to the running Container
later.

Alternatively, you can give the Container a specific name during startup, making it easier to
reference:

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

## Copy files between Host and Container

```bash
docker cp <containerId>:/file/path/within/container /host/path/target
```

## Stop a Container

```bash
docker stop my-container
```

## Print OS and Machine Infos

To debug multi-platform-image issues, it can be helpful to display the operating system and
architecture of the current machine using the following command:

```bash
python -c "import platform; print(platform.system(), platform.machine())"
```

Sample output:

```text
Darwin arm64
```
