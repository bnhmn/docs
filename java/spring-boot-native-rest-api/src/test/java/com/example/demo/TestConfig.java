package com.example.demo;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.example.demo.fixtures.DatabaseCleaner;
import com.example.demo.fixtures.SecurityFixtures;

import static com.example.demo.TestContainers.lookupMockOauthServerIssuerUrl;
import static com.example.demo.fixtures.SecurityFixtures.VALID_TOKEN;

@TestConfiguration
@ImportTestcontainers(TestContainers.class)
class TestConfig {

  @Bean
  RestTestClient anonymousTestClient(MockMvc mockMvc) {
    return RestTestClient.bindTo(mockMvc).build();
  }

  @Bean
  RestTestClient authorizedTestClient(MockMvc mockMvc) {
    return RestTestClient.bindTo(mockMvc)
        .defaultHeaders(headers -> headers.setBearerAuth(VALID_TOKEN.get()))
        .build();
  }

  @PostConstruct
  void setupSecurityFixtures() {
    SecurityFixtures.initialize(lookupMockOauthServerIssuerUrl());
  }

  @Bean
  DatabaseCleaner databaseCleaner(JdbcTemplate jdbcTemplate) {
    return new DatabaseCleaner(jdbcTemplate);
  }
}
