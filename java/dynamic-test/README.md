# Dynamic JUnit tests

You can create [dynamic test](https://docs.junit.org/6.0.2/writing-tests/dynamic-tests.html) in JUnit.

## Example Test

```java
package com.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class DynamicTest {

  /**
   * This test assumes that the subdirectories a, b, c, and d, containing test files, exist under
   * src/test/resources/testfiles. It scans these subdirectories and creates a dynamic test for each
   * file found. If a subdirectory is empty or does not exist, its test will report an error.
   */
  @TestFactory
  Stream<DynamicNode> shouldBeValidFiles() {
    return Stream.of("a", "b", "c", "d")
      .map(name -> loadTestFiles(name)
        .map(testFiles -> (DynamicNode)
          dynamicContainer(name, testFiles.stream()
            .map(testFile -> dynamicTest(testFile.getFileName().toString(), () -> {
              assertThat(testFile).content().isNotEmpty();
            }))))
        .orElse(dynamicTest(
            name,
            () -> fail("There are no test files in folder 'src/test/resources/testfiles/%s'".formatted(name)))));
  }

  @SneakyThrows
  private Optional<List<Path>> loadTestFiles(String name) {
    var testFilesFolder = Path.of(getClass().getClassLoader().getResource("testfiles").toURI());
    var subFolder = testFilesFolder.resolve(name);
    if (Files.isDirectory(subFolder)) {
      var testFiles = Files.walk(subFolder)
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().endsWith(".json"))
          .toList();
      if (!testFiles.isEmpty()) {
        return Optional.of(testFiles);
      }
    }
    return Optional.empty();
  }
}
```

This test factory produces a test structure that looks roughly like this:

```text
shouldBeValidFiles()
├── a
│   ├── a1.json
│   └── a2.json
├── b
│   ├── b1.json
│   └── b2.json
├── c
│   ├── c1.json
│   └── c2.json
└── d
```
