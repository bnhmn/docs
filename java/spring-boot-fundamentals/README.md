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

## Testing

### Load test profiles dynamically

```java
package com.example;

import org.springframework.test.context.ActiveProfilesResolver;

public class TestProfileResolver implements ActiveProfilesResolver {

    @Override
    public String[] resolve(Class<?> testClass) {
        String ci = System.getenv("CI");
        if ("true".equals(ci)) {
            // CI=true only when running on GitHub Actions.
            // Then, activate both the "test" and "test-ci" profiles.
            return new String[] { "test", "test-ci" };
        }
        // Otherwise, just activate the "test" profile.
        return new String[] { "test" };
    }
}
```

Register the resolver on the test class with
`@ActiveProfiles(resolver = TestProfileResolver.class)`.

### Configure multiple RestTestClients

When tests cover endpoints with different security requirements, define a `RestTestClient` for each
API. See
[RestTestClient documentation](https://docs.spring.io/spring-framework/reference/testing/resttestclient.html)
for details.

```java
package com.example;

import com.example.security.SecurityFixtures;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

@TestConfiguration
public class TestConfig {

    /** RestTestClient to test /api/** endpoints. */
    @Bean
    RestTestClient apiTestClient(MockMvc mockMvc, SecurityFixtures securityFixtures) {
        return RestTestClient.bindTo(mockMvc)
            .defaultHeaders(headers -> headers.setBearerAuth(securityFixtures.aValidApiToken()))
            .build();
    }

    /** RestTestClient to test /internal/** endpoints. */
    @Bean
    RestTestClient internalTestClient(MockMvc mockMvc, SecurityFixtures securityFixtures) {
        return RestTestClient.bindTo(mockMvc)
            .defaultHeaders(headers -> headers.setBearerAuth(securityFixtures.aValidIntToken()))
            .build();
    }
}
```

Import this configuration in the test class and inject the client through a constructor parameter
whose name matches the bean name, such as `apiTestClient` or `internalTestClient`.
