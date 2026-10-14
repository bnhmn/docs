import { heyApiPlugin } from "@hey-api/vite-plugin";
import tailwindcss from "@tailwindcss/vite";
import { devtools } from "@tanstack/devtools-vite";
import { tanstackRouter } from "@tanstack/router-plugin/vite";
import viteReact from "@vitejs/plugin-react";
import { defineConfig } from "vite";

// https://vite.dev/config/
export default defineConfig({
  resolve: { tsconfigPaths: true },
  plugins: [
    devtools(),
    tailwindcss(),
    // https://tanstack.com/router/latest/docs/installation/with-vite
    tanstackRouter({ target: "react", autoCodeSplitting: true }),
    viteReact(),
    // https://heyapi.dev/docs/openapi/typescript/get-started
    heyApiPlugin({
      config: {
        input: "https://dummyjson.com/openapi.json",
        output: "src/lib/api/generated",
        plugins: ["@hey-api/client-fetch", { name: "@tanstack/react-query", queryOptions: true }],
      },
    }),
  ],
});
