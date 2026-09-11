################################# Build Stage #################################
# https://github.com/graalvm/container
FROM ghcr.io/graalvm/native-image-community:25i3-25.0.4.1-ol10-20260825 AS build

# Since the GraalVM base image unfortunately does not include Maven, we have to install it here
COPY --from=maven:3.9.16-eclipse-temurin-25-alpine /usr/share/maven /usr/share/maven
RUN ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

WORKDIR /workspace
COPY src src
COPY pom.xml .

# This Maven command builds a native executable using GraalVM Native Build Tools as described here:
# https://docs.spring.io/spring-boot/how-to/native-image/developing-your-first-application.html
RUN --mount=type=cache,id=maven_repo,target=/root/.m2/repository \
    mvn native:compile -Pnative -DskipTests --batch-mode
# The 'mount=type=cache' option caches the Maven dependencies in Docker so they don't have to be downloaded
# again with every build: https://docs.docker.com/reference/dockerfile/#run---mounttypecache.
# If your CI system does not support this, you can also cache the m2 folder externally and mount it as standard folder:
# Dockerfile: RUN --mount=type=bind,from=maven_repo,source=.,target=/root/.m2/repository,rw \
# Command: docker build -t demo:latest --build-context maven_repo="$HOME/.m2/repository" .

################################ Runtime stage ################################
# https://github.com/GoogleContainerTools/distroless
FROM gcr.io/distroless/base-debian13:nonroot

COPY --from=build /workspace/target/demo /app

ENTRYPOINT ["/app"]
