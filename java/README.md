# Java

[Java](https://www.oracle.com/java/) is a high-level, object-oriented programming language widely used for building
cross-platform applications. It is designed to be platform-independent through the use of the Java Virtual Machine
(JVM), allowing developers to **write code once and run it anywhere**. Java is known for its robustness and large
ecosystem, making it popular for web development, enterprise software, and large systems.

## Resources

* [pom.xml](pom.xml)
  A basic pom with Maven Compiler, Maven Surfire and Maven Assembly plugin.
* [log4j2.xml](log4j2.xml)
  A log4j2 config with console and file output. Put this file into the classpath and add log4j2
  as Maven dependency to make it work.
* [logback.xml](logback.xml)
  A logback.xml config with console and file output. Put this file into the classpath and add
  logback as Maven dependency to make it work.

## Maven

[Maven](https://maven.apache.org/) is a popular build tool for Java. Although being popular,
its pom.xml files are quite verbose.

With [Gradle](https://docs.gradle.org/current/userguide/userguide.html) and its **Kotlin DSL**,
there is a more modern alternative that allows scripting.

### Show Dependency Tree

```bash
mvn dependency:tree
```

### Show Effective POM (including all dependencies and plugins)

```bash
mvn help:effective-pom
```
