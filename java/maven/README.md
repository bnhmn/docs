# Maven

[Maven](https://maven.apache.org/) is a build automation tool used mainly for [Java](..) projects. It simplifies the
build process, manages dependencies, and provides a standardized way to configure project setups using XML files.

With [Gradle](https://docs.gradle.org/current/userguide/userguide.html) and its **Kotlin DSL**,
there is a more modern alternative that allows scripting.

## Show Java Version, Path and Platform

```bash
mvn --version
```

## Show Dependency Tree

```bash
mvn dependency:tree
```

Get hints about decisions on dependency versions and scopes:

```bash
mvn dependency:tree -Dverbose | grep spring-boot-starter-webflux -A 10 -B 20
```

Note that a Maven project pom can override the scope of a transitive dependency brought in from a library, leading to
issues. The report above will show this.

## Maven Multi-Module Projects

In a multi-module Maven project, you can use the `-pl my-maven-module` option to run a Maven command for selected
modules only:

```bash
mvn dependency:tree -pl my-maven-module -Dverbose
```

## Show Effective POM (parent pom + project pom)

```bash
mvn help:effective-pom
```

## Build Without Tests

Use one of these commands:

```bash
mvn clean package -Dmaven.test.skip=true
```

```bash
MAVEN_ARGS=-Dmaven.test.skip=true mvn clean package
```

## Environment Variables

See <https://maven.apache.org/configure.html>.

`MAVEN_OPTS` environment variable:
This variable contains parameters used to start up the JVM running Maven and can be used to supply additional options
to it. E.g. JVM memory settings can be defined with the value *-Xms256m -Xmx512m*.

`MAVEN_ARGS` environment variable:
Starting with Maven 3.9.0, this variable contains arguments passed to Maven before CLI arguments. E.g., options and
goals could be defined with the value *-B -V checkstyle:checkstyle*.

## Maven Dependency Check

Maven [DependencyCheck](https://github.com/dependency-check/DependencyCheck) is a software composition analysis utility
that detects publicly disclosed vulnerabilities in application dependencies.

Run using the official [NVD API](https://nvd.nist.gov/developers/vulnerabilities):

```bash
mvn -Pdependency-check dependency-check:aggregate -Dformats=HTML,JSON -DnvdApiKey="$NVD_API_KEY"
```

Run using the [cveb-in](https://cveb.in/) mirror:

```bash
mvn -Pdependency-check dependency-check:aggregate -Dformats=HTML,JSON -DnvdDatafeedUrl="https://mirror.cveb.in/nvd/json/cve/1.1/nvdcve-1.1-{0}.json.gz"
```
