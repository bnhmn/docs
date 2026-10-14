import react from "@vitejs/plugin-react";
import { defineConfig } from "vitest/config";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  test: {
    environment: "jsdom",
    setupFiles: ["./vitest.setup.ts"],
    // Speed up tests: components are loaded once per worker, not once per file; tests stay isolated
    pool: "vmThreads",
    coverage: {
      provider: "v8",
      include: ["src/**/*.{ts,tsx}"],
      exclude: ["src/**/generated/**", "src/**/*.gen.ts"],
      reporter: ["text", "lcov"], // SonarQube reads "lcov"
    },
  },
});
