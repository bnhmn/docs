package com.example.webapp.rest.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class ApiConfig {

  @Bean
  RestClient apiRestClient(ApiProperties apiProperties, OAuth2AuthorizedClientManager manager) {
    return RestClient.builder()
        .baseUrl(apiProperties.getBaseUrl())
        .requestInterceptor(new OAuth2ClientHttpRequestInterceptor(manager))
        .build();
  }
}
