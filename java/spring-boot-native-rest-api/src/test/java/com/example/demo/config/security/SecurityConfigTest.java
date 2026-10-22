package com.example.demo.config.security;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.example.demo.IntegrationTest;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static com.example.demo.fixtures.SecurityFixtures.*;

@IntegrationTest
public class SecurityConfigTest {

  @Autowired
  private RestTestClient anonymousTestClient;

  @ParameterizedTest
  @ValueSource(
      strings = {"/actuator/health", "/actuator/health/readiness", "/actuator/health/liveness"})
  void healthEndpointsArePublic(String uri) {
    anonymousTestClient.get().uri(uri).exchange().expectStatus().isOk();
  }

  @ParameterizedTest
  @ValueSource(strings = {"/me/playlist"})
  void apiWithoutTokenIsNotAccessible(String uri) {
    anonymousTestClient.get().uri(uri).exchange().expectStatus().isUnauthorized();
  }

  @ParameterizedTest
  @ValueSource(strings = {"/me/playlist"})
  void apiWithValidTokenIsAccessible(String uri) {
    anonymousTestClient
        .get()
        .uri(uri)
        .headers(headers -> headers.setBearerAuth(VALID_TOKEN.get()))
        .exchange()
        .expectStatus()
        .isOk();
  }

  @ParameterizedTest
  @MethodSource("invalidTokens")
  void apiWithInvalidTokenIsNotAccessible(Supplier<String> bearerToken, int expectedStatus) {
    anonymousTestClient
        .get()
        .uri("/me/playlist")
        .headers(headers -> headers.setBearerAuth(bearerToken.get()))
        .exchange()
        .expectStatus()
        .isEqualTo(expectedStatus);
  }

  private static Stream<Arguments> invalidTokens() {
    return Stream.of(
        arguments(INVALID_TOKEN_EMPTY_STRING, 401),
        arguments(INVALID_TOKEN_WRONG_AUDIENCE, 401),
        arguments(INVALID_TOKEN_NO_PLAYLIST_SCOPE, 403));
  }
}
