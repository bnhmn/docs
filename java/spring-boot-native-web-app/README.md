# spring-boot-native-web-app

This is a minimal web app consisting of a React frontend (SPA) served by a Spring Boot backend (BFF), following the
[Backend-for-Frontend pattern](https://www.rfc-editor.org/rfc/rfc10017.html#name-backend-for-frontend-bff).
The SPA communicates only with the BFF, which provides a uniform API for the SPA and handles authentication, session
management, and security. As a thin proxy, it calls dedicated services via REST APIs, where the actual business logic
resides. Tokens stay server-side and the browser only receives the session cookie.

```text
spring-boot-native-web-app/
├─ frontend/                        React SPA
├─ backend/                         Spring Boot BFF
│  └─ src/main/resources/
│     ├─ application.yaml           shared default configuration
│     ├─ application-cloud.yaml     cloud configuration (dev/staging/prod)
│     └─ application-local.yaml     local configuration
└── docker-compose.yaml             local infrastructure, e.g. Redis
```

## Quick Start

1. Make sure you have [Java 25](https://www.oracle.com/java/technologies/downloads/),
   [Maven](https://maven.apache.org/download.cgi), and
   [Docker](https://docs.docker.com/engine/install/) installed.
2. Run `mvn compile` to build the frontend.
3. Run `mvn -f backend spring-boot:run -Dspring-boot.run.profiles=local` to start the backend.
4. Go to http://localhost:8080 in your browser. You should see the login page.
5. Enter a random username and submit. You should be redirected to the main page of the app.
6. Note that you can log out via http://localhost:8080/logout if you want to repeat the login flow.

## How to Test

Run `mvn verify` to execute all tests.

## How to Build

Build [GraalVM native Docker image](https://docs.spring.io/spring-boot/reference/packaging/native-image/introducing-graalvm-native-images.html)
(preferred option):

```bash
mvn -f frontend compile &&
mvn -f backend spring-boot:build-image -Pnative
```

Build GraalVM native Docker image (alternative option):

```bash
docker build -t backend:0.0.1-SNAPSHOT .
```

Run image:

```bash
docker run --rm -e SPRING_PROFILES_ACTIVE=local --network host backend:0.0.1-SNAPSHOT
```

## How to Contribute

### Branching Strategy and Deployment

If you want to make code changes, you first need to create a new feature branch from the `main`
branch. Once a feature is complete, you can open a pull request to merge the feature branch into the
main branch. The main branch is continuously deployed to the `dev` environment for testing.

### Code Formatting

- **Frontend**: Format with [Biome](https://biomejs.dev/guides/getting-started/) via IDE or
  `npm --prefix frontend run fix`.
- **Backend**: Format with [Spotless](https://github.com/diffplug/spotless) via
  [IntelliJ commit check](https://github.com/lipiridi/spotless-applier/blob/main/PRE_COMMIT_CHECK.md)
  or `mvn -f backend spotless:apply`. When using the IntelliJ commit check, you should disable the
  following other commit checks to avoid conflicts:
  `Reformat code, Rearrange code, Optimize imports, Cleanup`.

## How to Recreate

<!-- prettier-ignore -->
> [!TIP]
> To scaffold a similar project, generate both project skeletons as follows.
>
> **Frontend**
>
> Run `npm create vite@latest frontend` and use these settings: `Framework: React, Variant: TypeScript`.
>
> **Backend**
>
> 1. Open [start.spring.io](https://start.spring.io/) and use these settings: `Project: Maven, Configuration: YAML, Java: latest LTS version`.
> 2. Add these dependencies: `Spring Web, Spring Security, OAuth2 Client, Spring Session Redis, Spring Boot Actuator, and GraalVM Native Support`.
> 3. Generate the project and extract the ZIP contents directly into the `backend` directory.
>
> Afterwards, copy or adapt the configuration and application-specific code from this repository.

## More Information

### Security, Redis, and Session

- OIDC login is provided by Spring Security OAuth2 Client. Authorization is handled by Spring
  Security authorities.
- Browser clients only receive a server-managed session cookie; access/refresh tokens stay server-side.
- Redis is required for Spring Session and OAuth2 authorized-client persistence.
- Session defaults: cookie "Secure=true', "Samesite=Lax", timeout "14d'; CSRF is enabled for
  state-changing browser endpoints.
