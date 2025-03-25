import type { TestProject } from "vitest/node";
import { startServer } from "./src/server";

// https://vitest.dev/config/#globalsetup

export default async function (project: TestProject) {
  const server = await startServer();
  const serverUrl = `http://localhost:${server.server.address()?.port}`;
  project.provide("serverUrl", serverUrl);
  return async () => await server.close();
}

declare module "vitest" {
  export interface ProvidedContext {
    serverUrl: string;
  }
}
