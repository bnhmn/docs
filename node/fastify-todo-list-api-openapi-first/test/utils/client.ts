import { inject } from "vitest";

import { createClient, createConfig } from "@hey-api/client-fetch";

export const client = createClient(
  createConfig({
    baseUrl: inject("serverUrl"),
  }),
);
