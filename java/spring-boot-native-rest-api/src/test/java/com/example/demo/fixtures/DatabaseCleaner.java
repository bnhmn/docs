package com.example.demo.fixtures;

import java.util.Collections;
import java.util.List;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;

/** Resets the database after each test so that the tests always start in a clean state. */
@Aspect
@TestComponent
@RequiredArgsConstructor
@Slf4j
public class DatabaseCleaner {

  private final JdbcTemplate jdbcTemplate;
  private List<String> tableNames = Collections.emptyList();
  private boolean resetRequired = true;

  @PostConstruct
  private void queryTableNames() {
    tableNames = jdbcTemplate.query("""
            SELECT table_name
            FROM information_schema.tables
            WHERE table_schema = 'public'
            AND table_name NOT IN ('flyway_schema_history')
            """, (row, number) -> row.getString("table_name"));
  }

  @After("execution(* org.springframework.data.jpa.repository.JpaRepository+.save*(*))")
  private void afterDatabaseWasModified() {
    resetRequired = true;
  }

  public void reset() {
    if (resetRequired) {
      log.debug("Resetting database tables");
      jdbcTemplate.execute("TRUNCATE TABLE %s CASCADE;".formatted(String.join(",", tableNames)));
      resetRequired = false;
    }
  }
}
