import { type ClientOptions, createClient, createConfig } from "./generated/client";

export * as query from "./generated/@tanstack/react-query.gen";
export * as api from "./generated/sdk.gen";
export * from "./generated/types.gen";

// This is a type-safe REST client automatically generated from the backend's OpenAPI specification
// https://heyapi.dev/docs/openapi/typescript/get-started

export const client = createClient(
  createConfig<ClientOptions>({
    baseUrl: import.meta.env.VITE_API_BASE_URL,
    // auth: () => "dummy-access-token", // bearer token placeholder
    // credentials: "same-origin",       // cookie authentication
  }),
);
