package com.example.demo;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * In integration tests, we generally test against <a
 * href="https://docs.spring.io/spring-boot/reference/testing/testcontainers.html">test
 * containers</a> rather than real infrastructure. This ensures we are not dependent on the
 * availability of external systems and don't need to use real credentials in tests.
 */
@SuppressWarnings("resource")
class TestContainers {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer postgres = new PostgreSQLContainer(
          DockerImageName.parse("postgres:18.6-alpine3.24"))
      .waitingFor(Wait.forListeningPort());

  @Container
  static final GenericContainer<?> mockOAuthServer =
      // This is used to mock an OIDC identity provider such as Auth0, Okta, or Entra ID
      // https://github.com/navikt/mock-oauth2-server
      new GenericContainer<>(DockerImageName.parse("ghcr.io/navikt/mock-oauth2-server:6.1.3"))
          .withExposedPorts(8080)
          .withEnv(
              "JSON_CONFIG",
              // language=json
              """
              {
                "tokenCallbacks": [
                  {
                    "issuerId": "default",
                    "requestMappings": [
                      {
                        "requestParam": "grant_type",
                        "match": "*",
                        "claims": {
                          "aud": "${audience}",
                          "sub": "${subject}",
                          "scp": "${scope}"
                        }
                      }
                    ]
                  }
                ]
              }
              """)
          .waitingFor(Wait.forHttp("/isalive"));

  @DynamicPropertySource
  static void oauthProperties(DynamicPropertyRegistry registry) {
    registry.add(
        "spring.security.oauth2.resourceserver.jwt.issuer-uri",
        TestContainers::lookupMockOauthServerIssuerUrl);
  }

  /**
   * Retrieves the issuer URL of the mock OAuth server. Note that this only works in integration
   * tests, as it requires the test containers to have been started.
   */
  static String lookupMockOauthServerIssuerUrl() {
    return "http://" + mockOAuthServer.getHost() + ":" + mockOAuthServer.getMappedPort(8080)
        + "/default";
  }
}
