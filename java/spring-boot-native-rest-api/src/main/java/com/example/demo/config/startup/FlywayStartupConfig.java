package com.example.demo.config.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.flyway.autoconfigure.FlywayMigrationStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configures Flyway migrations and controls whether the application continues running after
 * database migrations.
 *
 * <p>Flyway migrations are only executed if {@code spring.flyway.enabled=true} at runtime.
 *
 * <p>After successful migrations, the application will be terminated if
 * {@code app.flyway.exit-after-migration=true}. Otherwise, it will continue running. This allows
 * the same native image to run migrations as a one-off task or to start normally without exiting.
 */
@Configuration
public class FlywayStartupConfig {

  @Bean
  FlywayMigrationStrategy flywayMigrationStrategy(
      Environment environment,
      FlywayStartupProperties properties,
      ApplicationContext applicationContext) {
    return flyway -> {
      // This guard is needed because FlywayAutoConfiguration only evaluates that feature toggle
      // at build time in a native image, not at runtime.
      var flywayEnabled = environment.getProperty("spring.flyway.enabled", Boolean.class, true);
      if (flywayEnabled) {
        flyway.migrate();
        if (properties.isExitAfterMigration()) {
          int exitCode = SpringApplication.exit(applicationContext, () -> 0);
          System.exit(exitCode);
        }
      }
    };
  }
}
