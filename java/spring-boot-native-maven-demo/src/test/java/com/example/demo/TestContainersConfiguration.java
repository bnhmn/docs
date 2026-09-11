package com.example.demo;

import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
class TestContainersConfiguration {

  // This is actually useless, since the current application logic does not use Redis at all.
  // However, this is simply intended to demonstrate how to set up a test container to test
  // against a real Redis/Postgres container instance during integration tests.
  // See https://docs.spring.io/spring-boot/reference/testing/testcontainers.html
  //  @Bean
  //  @ServiceConnection(name = "redis")
  //  GenericContainer<?> redisContainer() {
  //    return new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
  //  }
}
