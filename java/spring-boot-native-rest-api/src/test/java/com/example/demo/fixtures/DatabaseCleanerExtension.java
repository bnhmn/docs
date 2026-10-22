package com.example.demo.fixtures;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/** Resets the database after each test so that the tests always start in a clean state. */
@Slf4j
public class DatabaseCleanerExtension implements BeforeAllCallback, AfterEachCallback {

  private DatabaseCleaner databaseCleaner;

  @Override
  public void beforeAll(@NonNull ExtensionContext extensionContext) throws Exception {
    databaseCleaner =
        SpringExtension.getApplicationContext(extensionContext).getBean(DatabaseCleaner.class);
    reset();
  }

  @Override
  public void afterEach(@NonNull ExtensionContext extensionContext) throws Exception {
    reset();
  }

  private void reset() throws Exception {
    databaseCleaner.reset();
    log.trace("All test components were reset");
  }
}
