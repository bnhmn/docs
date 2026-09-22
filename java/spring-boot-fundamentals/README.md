# Spring Boot

[Spring Boot](https://spring.io/projects/spring-boot) is a framework used to build Java-based
enterprise applications. It simplifies the setup of components needed in typical enterprise
applications, providing solutions for web interfaces, security, validation, data access, batch
processing, message-driven communication, documentation, and more. Its aim is to let developers
focus more on application functionality instead of application setup.

## Quick Start

Use [Spring Initializr](https://start.spring.io/) to quickly generate a runnable Spring Boot
project.

Example configuration:

```text
Project: Maven
Language: Java
Spring Boot: 4.1.1
Configuration: YAML
Java: 25
Dependencies: Spring Web, Spring Data JPA, Lombok, etc.
```

## Supported Versions

See [Spring Boot Support](https://spring.io/projects/spring-boot#support) for currently supported
versions.

## Documentation

- [Spring Boot](https://docs.spring.io/spring-boot/index.html)
- [Spring Framework](https://spring.io/projects/spring-framework#learn)
- [Spring Data](https://spring.io/projects/spring-data)
- [Spring Security](https://spring.io/projects/spring-security#learn)

## developer.yaml files

```yaml
spring:
  config:
    # This imports values from the 'developer.yaml' or any 'developer-[profile].yaml' file in the 'resources' folder.
    # There you can store configurations and secrets for testing purposes that will not be pushed to the repository.
    # https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.files.importing
    import: optional:classpath:developer.yaml
```

Make sure to extend your `.gitignore` file with:

```gitignore
developer.yaml
developer-*.yaml
```
