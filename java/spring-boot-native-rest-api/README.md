# spring-boot-native-rest-api

This is a minimal example of a Spring Boot native-image-ready REST API. It follows an API-first
approach: [openapi.yaml](openapi.yaml) defines the API, and Maven generates the server code during
compilation. The application uses OAuth JWTs for authentication, PostgreSQL for data storage, and
Flyway to manage database migrations. The API lets each authenticated user view their personal
playlist, add songs, and remove them.

```text
spring-boot-native-rest-api/
├─ src/main/resources/              
│  ├─ application.yaml           shared default configuration
│  ├─ application-cloud.yaml     cloud configuration (dev/staging/prod)
│  └─ application-local.yaml     local configuration
├─ compose.yaml                  local infrastructure, e.g. Postgres
└─ openapi.yaml                  API specification
```

## Quick Start

1. Make sure you have [Java 25](https://www.oracle.com/java/technologies/downloads/),
   [Maven](https://maven.apache.org/download.cgi), and
   [Docker](https://docs.docker.com/engine/install/) installed.
2. Run `mvn spring-boot:run -Dspring-boot.run.profiles=local` to start the application in local
   mode.
3. Run `curl http://localhost:8080/actuator/health` to verify that the server is running.
4. Run these commands to request an auth token and read your playlist:
   ```bash
   access_token=$(curl -sSfX POST http://localhost:3000/default/token -d grant_type=client_credentials -d client_id=dummyclient -d client_secret=dummysecret -d audience=https://myapi.example.com -d subject=john123 -d scope=playlist | jq '.access_token' --raw-output)
   curl -sSf http://localhost:8080/me/playlist -H "Authorization: Bearer $access_token" | jq
   ```
5. Run this command to add a song to your playlist:
   ```bash
   curl -sSfX POST http://localhost:8080/me/playlist/songs -H "Authorization: Bearer $access_token" -H "Content-Type: application/json" -d '{"title":"Dancing Queen","artist":"ABBA"}' | jq
   ```
6. Run this command to delete the first song from your playlist:
   ```bash
   first_song_id=$(curl -sSf http://localhost:8080/me/playlist -H "Authorization: Bearer $access_token" | jq '.songs[0].id' --raw-output)
   curl -sSfX DELETE http://localhost:8080/me/playlist/songs/$first_song_id -H "Authorization: Bearer $access_token" | jq
   ```

## How to Test

Run `mvn verify` to execute the test suite.

## How to Build

Build [GraalVM native Docker image](https://docs.spring.io/spring-boot/reference/packaging/native-image/introducing-graalvm-native-images.html)
(preferred option):

```bash
mvn spring-boot:build-image -Pnative
```

Build GraalVM native Docker image (alternative option):

```bash
docker build -t rest-api:0.0.1-SNAPSHOT .
```

Run image in web server mode:

```bash
docker run --rm -e SPRING_PROFILES_ACTIVE=local -e SPRING_FLYWAY_ENABLED=false --network host rest-api:0.0.1-SNAPSHOT
```

Run image in database migration mode (apply migrations and exit):

```bash
docker run --rm -e SPRING_PROFILES_ACTIVE=local -e SPRING_FLYWAY_ENABLED=true -e APP_FLYWAY_EXIT_AFTER_MIGRATION=true --network host rest-api:0.0.1-SNAPSHOT
```

## How to Contribute

### Branching Strategy and Deployment

If you want to make code changes, you first need to create a new feature branch from the `main`
branch. Once a feature is complete, you can open a pull request to merge the feature branch into the
main branch. The main branch is continuously deployed to the `dev` environment for testing.

### Code Formatting

Format with [Spotless](https://github.com/diffplug/spotless)
via [IntelliJ commit check](https://github.com/lipiridi/spotless-applier/blob/main/PRE_COMMIT_CHECK.md)
or `mvn spotless:apply`. When using the IntelliJ commit check, you should disable the following other commit checks to
avoid conflicts: `Reformat code, Rearrange code, Optimize imports, Cleanup`.

## How to Recreate

<!-- prettier-ignore -->
> [!TIP]
> To scaffold a similar project, generate the project skeleton as follows:
>
> 1. Open [start.spring.io](https://start.spring.io/) and use these settings:
     `Project: Maven, Configuration: YAML, Java: latest LTS version`.
> 2. Add these dependencies: `GraalVM Native Support, Spring Web, Validation, Spring Security, OAuth2 Resource Server,
>    Spring Data JPA, Flyway Migration, PostgreSQL Driver, Spring Boot Actuator, Lombok, Docker Compose Support,
>    Testcontainers, Spring Configuration Processor`.
> 3. Generate the project and extract the ZIP contents directly into the project root directory.
>
> Afterwards, copy or adapt the configuration and application-specific code from this repository.

## More Information

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.1.1/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.1.1/maven-plugin/build-image.html)
* [GraalVM Native Image Support](https://docs.spring.io/spring-boot/4.1.1/reference/packaging/native-image/introducing-graalvm-native-images.html)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/4.1.1/reference/testing/testcontainers.html#testing.testcontainers)
* [Testcontainers Postgres Module Reference Guide](https://java.testcontainers.org/modules/databases/postgres/)
* [Spring Web](https://docs.spring.io/spring-boot/4.1.1/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.1.1/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/4.1.1/reference/web/spring-security.html)
* [OAuth2 Resource Server](https://docs.spring.io/spring-boot/4.1.1/reference/web/spring-security.html#web.security.oauth2.server)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/4.1.1/reference/actuator/index.html)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/4.1.1/specification/configuration-metadata/annotation-processor.html)
* [Docker Compose Support](https://docs.spring.io/spring-boot/4.1.1/reference/features/dev-services.html#features.dev-services.docker-compose)
* [Testcontainers](https://java.testcontainers.org/)
* [Flyway Migration](https://docs.spring.io/spring-boot/4.1.1/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Additional Links

These additional references should also help you:

- [Configure AOT settings in Build Plugin](https://docs.spring.io/spring-boot/4.0.8/how-to/aot.html)
