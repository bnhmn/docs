# React App

A [React](https://react.dev/) single-page application (SPA), written in
[TypeScript](https://www.typescriptlang.org/) and built with [Vite](https://vite.dev/).

## Quick Start

1. Ensure [Node.js](https://nodejs.org/en) is installed on your machine.
2. Run `npm ci` to install the dependencies.
3. Run `npm run dev` to start the development server.

## Commands

- `npm test` - execute tests
- `npm run build` - build production bundle
- `npm run fix` - format code and fix code style

## Recommended Tools

- [TanStack Router](https://tanstack.com/router/latest/docs/quick-start) - client-side routing for
  pages and routes
- [TanStack Query](https://tanstack.com/query/latest/docs/framework/react/quick-start) - backend
  data fetching and caching
- [chakra](https://v2.chakra-ui.com/) - [MUI](https://mui.com/) - [shadcn](https://ui.shadcn.com/) -
  [tailwind](https://tailwindcss.com/) - UI libraries
- [Hey API](https://heyapi.dev/) - generates TypeScript types from the backend OpenAPI specification
- [Biome](https://biomejs.dev/) - formatting and linting
- [Vitest](https://vitest.dev/guide/) - test framework
- [MSW](https://mswjs.io/docs/quick-start) - mock backend APIs in the browser during local
  development

## Authentication with the BFF

Since the [Backend for Frontend](../README.md) is used for authentication, all requests must include
the session cookie.

For example, this configuration is suitable for a
[Hey API client](https://heyapi.dev/docs/openapi/typescript/clients/fetch).

```ts
import { createClient, createConfig, type ClientOptions } from "./generated/client";
export * as backend from "./generated/sdk.gen";
export * from "./generated/types.gen";

export const client = createClient(
  createConfig<ClientOptions>({
    baseUrl: "http://localhost:8080/api",
    // Send the session cookie along with all requests to the backend
    credentials: "same-origin",
  }),
);

/**
 * Reads the CSRF token from Spring Security 'XSRF-TOKEN' cookie.
 * Read freshly on every call, the backend may rotate it.
 */
function readCsrfToken(): string | undefined {
  const value = document.cookie
    .split("; ")
    .find((part) => part.startsWith("XSRF-TOKEN="))
    ?.split("=")[1];
  return value ? decodeURIComponent(value) : undefined;
}

// The CSRF token must be included with all state-changing requests (POST, PATCH, DELETE)
client.interceptors.request.use((request) => {
  const csrfToken = readCsrfToken();
  if (csrfToken) {
    request.headers.set("X-XSRF-TOKEN", csrfToken);
  }
  return request;
});

// A 401 response usually means that the user's session has expired and a new login is required.
// Refresh the page to start the login; the guard prevents multiple redirects from concurrent requests
let loginStarted = false;

client.interceptors.response.use((response) => {
  if (response.status === 401 && !loginStarted) {
    loginStarted = true;
    window.location.reload();
  }
  return response;
});
```

### Logout handler

Submit logout as a native form so the browser follows the BFF's redirect to the identity provider. A
`fetch` request does not navigate the browser.

```ts
import { readCsrfToken } from "./csrf";

/**
 * Signs the user out via POST /logout. The browser submits the session cookie and CSRF token,
 * then follows the redirect to the identity provider.
 */
export function logout(): void {
  const form = document.createElement("form");
  form.method = "POST";
  form.action = "http://localhost:8080/logout";

  const csrfToken = readCsrfToken();
  if (csrfToken) {
    const input = document.createElement("input");
    input.type = "hidden";
    input.name = "_csrf";
    input.value = xorEncode(csrfToken);
    form.appendChild(input);
  }

  document.body.appendChild(form);
  form.submit();
}

/**
 * Encodes the token in Spring Security's BREACH-protected form format.
 * @see https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html
 */
function xorEncode(token: string): string {
  const tokenBytes = new TextEncoder().encode(token);
  const random = crypto.getRandomValues(new Uint8Array(tokenBytes.length));
  const combined = new Uint8Array(tokenBytes.length * 2);

  combined.set(random);
  tokenBytes.forEach((byte, index) => {
    combined[tokenBytes.length + index] = byte ^ random[index];
  });

  const binary = Array.from(combined, (byte) => String.fromCharCode(byte)).join("");
  return btoa(binary).replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/, "");
}
```
