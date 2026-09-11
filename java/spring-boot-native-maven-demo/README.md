# Spring Boot Native Maven Demo

This project is a minimal Spring Boot application that demonstrates how to build a native executable or container using
[GraalVM Native Images](https://docs.spring.io/spring-boot/reference/packaging/native-image/introducing-graalvm-native-images.html).

It provides a simple HTTP endpoint at `http://localhost:8080` that returns a static `Hello, World!` response,
proving that the service is running and reachable.

> [!TIP]
> If you want to start from scratch, the easiest way to create a new native Spring Boot project is to visit 
> [start.spring.io](https://start.spring.io/), add the dependency for GraalVM Native Support, and generate the project.

## Quick Start

1. Make sure you have [Java 25](https://www.oracle.com/java/technologies/downloads/),
   [Maven](https://maven.apache.org/download.cgi), and [Docker](https://docs.docker.com/engine/install/) installed.
2. Configure Maven as described here: <https://example.com/maven-corporate-setup-guide>.
3. Run `mvn spring-boot:run -Dspring-boot.run.profiles=local` to start the application in local mode.
4. Run `curl http://localhost:8080` to verify that the server is running.

## How to Test

Run `mvn verify` to execute the test suite.

## How to Build

There are two ways to [build a Spring Boot native application](https://docs.spring.io/spring-boot/how-to/native-image/developing-your-first-application.html): using Cloud Native Buildpacks or GraalVM Native
Build Tools. Both variants presented below use Docker to create an image that can then be executed like any other
container.

### Buildpacks

This is the easiest option if internet access is available, as it downloads all required dependencies at build time:

```bash
mvn spring-boot:build-image -Pnative
```

### GraalVM in Docker

This approach is suitable if you don't have internet access during the build, as all dependencies are included in
the [Docker builder image](Dockerfile). You just need to ensure that this image is available via the corporate registry:

```bash
docker build -t demo:0.0.1-SNAPSHOT .
```

### Run the Docker Image

Run the generated image like any other container:

```bash
docker run --rm -p 8080:8080 demo:0.0.1-SNAPSHOT
```

### Run Tests in Native Image

You can also run your existing test suite in a native image.
This is an efficient way to validate the compatibility of your application.

To do this, you need a GraalVM distribution installed locally. You can download one from the [Liberica Native Image Kit](https://bell-sw.com/pages/downloads/native-image-kit) page.

Then, run the tests in native image mode:

```bash
mvn verify -PnativeTest
```

## How to Contribute

### Packaging Strategy

### Branching Strategy and Deployment

If you want to make code changes, you first need to create a new feature branch from the `main` branch.
Once a feature is complete, you can open a pull request to merge the feature branch into the main branch.
The main branch is continuously deployed to the `dev` environment for testing.

### Code Formatting

This project uses [Spotless](https://github.com/diffplug/spotless) to automate code formatting.
Run `mvn spotless:apply` to trigger code formatting via the command line.

You can also set up an [IntelliJ commit check](https://github.com/lipiridi/spotless-applier/blob/main/PRE_COMMIT_CHECK.md)
that will format your code on each commit. Disable these other commit checks to avoid conflicts
with Spotless: `Reformat code, Rearrange code, Optimize imports, Cleanup`.

## More Information

### Testcontainers support

This project uses [Testcontainers at development time](https://docs.spring.io/spring-boot/4.0.8/reference/features/dev-services.html#features.dev-services.testcontainers).

Testcontainers uses the following Docker image: [`redis:latest`](https://hub.docker.com/_/redis).
For reproducible builds, replace `latest` with a fixed tag matching the production version.

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.8/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.8/maven-plugin/build-image.html)
* [GraalVM Native Image Support](https://docs.spring.io/spring-boot/4.0.8/reference/packaging/native-image/introducing-graalvm-native-images.html)
* [Azure Actuator](https://aka.ms/spring/docs/actuator)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/4.0.8/reference/testing/testcontainers.html#testing.testcontainers)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/4.0.8/reference/actuator/index.html)
* [Spring Security](https://docs.spring.io/spring-boot/4.0.8/reference/web/spring-security.html)
* [Validation](https://docs.spring.io/spring-boot/4.0.8/reference/io/validation.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.0.8/reference/web/servlet.html)
* [Azure Active Directory](https://microsoft.github.io/spring-cloud-azure/current/reference/html/index.html#spring-security-with-azure-active-directory)
* [Spring Session for Spring Data Redis](https://docs.spring.io/spring-session/reference/)
* [Testcontainers](https://java.testcontainers.org/)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Java Web App with the Spring Boot Starter for Azure Active Directory](https://aka.ms/spring/msdocs/aad)

### Additional Links

These additional references should also help you:

* [Configure AOT settings in Build Plugin](https://docs.spring.io/spring-boot/4.0.8/how-to/aot.html)
* [Azure Active Directory Sample](https://aka.ms/spring/samples/latest/aad)
