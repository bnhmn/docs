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

Here are some common instructions used in a Dockerfile:

* **FROM** - Set the base Image (Images can be found in the [Docker Hub](https://hub.docker.com/))
* **RUN** - Run shell commands
* **WORKDIR** - Set the working directory
* **COPY** - Copy local files into the Image
* **ENV** - Set environment variables
* **CMD** - Set the start command

## Build an Image

To build an Image, open a terminal inside the directory containing the Dockerfile and execute the following command:

```bash
docker build .
```

Docker executes the instructions from the Dockerfile in sequence and creates the Image accordingly. 
The resulting Image is identified by a unique SHA256 hash.

To upload an Image to a repository, it's required to assign it a **Tag**. 
A Tag is an alternative name for the Image and typically follows the pattern `<name>:<version>`.

To build the Image with the specific Tag `java-app:1.0.0`, use the following command:

```bash
docker build -t java-app:1.0.0 .
```

## Execute an Image

Execute an Image (aka run a Container) using

```bash
docker run -it java-app:1.0.0
```

Execute an Image with a custom command

```bash
docker run -it java-app:1.0.0
```

