package com.example.demo.config.startup;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("app.flyway")
public class FlywayStartupProperties {

  /**
   * Normally, the web server starts and keeps running. If this is enabled, the application applies
   * database migrations and then terminates.
   */
  private boolean exitAfterMigration = false;
}
