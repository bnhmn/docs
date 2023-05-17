# Maven

[Maven](https://maven.apache.org/) is a popular build tool for Java.
Although being popular, its pom.xml files are quite verbose.

With [Gradle](https://docs.gradle.org/current/userguide/userguide.html)
and its **Kotlin DSL** there is a more modern and concise alternative that allows scripting.

## Show Dependency Tree

```bash
mvn dependency:tree
```

## Show Effective POM (including all dependencies and plugins)

```bash
mvn help:effective-pom
```
