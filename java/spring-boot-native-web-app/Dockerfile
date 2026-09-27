################################# Build Stage #################################
# https://github.com/graalvm/container
FROM ghcr.io/graalvm/native-image-community:25i3-25.0.4.1-ol10-20260825 AS build

# Since the GraalVM base image unfortunately does not include Maven, we have to install it here
COPY --from=maven:3.9.16-eclipse-temurin-25-alpine /usr/share/maven /usr/share/maven
RUN ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

WORKDIR /workspace
COPY . .

# We must enable a production-like Spring profile at build time so that Spring can bake all required
# auto-configured components into the native image. In our case, we use the 'test' profile because
# it exercises all relevant infrastructure (e.g. Redis, OAvth2 client, security).
# This does not imply that the test profile is used at runtime. There, of course, it remains disabled.
ARG SPRING_PROFILES_ACTIVE=test
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE

# This Maven command builds a native executable using GraalVM Native Build Tools as described here:
# https://docs.spring.io/spring-boot/how-to/native-image/developing-your-first-application.html
RUN --mount=type=cache,id=maven_repo,target=/root/.m2/repository \
    mvn --batch-mode -f frontend compile && \
    mvn --batch-mode -f backend native:compile -Pnative -DskipTests
# The 'mount=type=cache' option caches the Maven dependencies in Docker so they don't have to be downloaded
# again with every build: https://docs.docker.com/reference/dockerfile/#run---mounttypecache.
# If your CI system does not support this, you can also cache the m2 folder externally and mount it as standard folder:
# Dockerfile: RUN --mount=type=bind,from=maven_repo,source=.,target=/root/.m2/repository,rw \
# Command: docker build -t demo:latest --build-context maven_repo="$HOME/.m2/repository" .

################################ Runtime stage ################################
# https://github.com/GoogleContainerTools/distroless
FROM gcr.io/distroless/base-debian13:nonroot

WORKDIR /app

COPY --from=build /workspace/backend/target/backend /app/backend
COPY --from=build /workspace/frontend/dist/index.html /app/public/index.html
COPY --from=build /workspace/frontend/dist/assets/ /app/public/assets/

ENTRYPOINT ["/app/backend"]
