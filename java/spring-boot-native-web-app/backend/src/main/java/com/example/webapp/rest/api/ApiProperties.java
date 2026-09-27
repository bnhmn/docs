package com.example.webapp.rest.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/** Configuration for the connected backend systems. */
@Data
@Validated
@ConfigurationProperties("app.backend")
public class ApiProperties {

  /** Backend API base URL. */
  private String baseUrl;
}
