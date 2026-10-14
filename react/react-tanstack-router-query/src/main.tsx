import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createRouter, RouterProvider } from "@tanstack/react-router";
import ReactDOM from "react-dom/client";
import { routeTree } from "./routeTree.gen";

// TanStack Query manages server state: it fetches data & handles caching and background refresh.
// https://tanstack.com/query/latest/docs/framework/react/quick-start

const queryClient = new QueryClient({
  defaultOptions: { queries: { retry: false } },
});

// TanStack Router is a is a React routing library with file-based routes and type-safe navigation.
// https://tanstack.com/router/latest/docs/routing/routing-concepts

const router = createRouter({
  routeTree,
  context: { queryClient },
  defaultPreload: "intent",
  scrollRestoration: true,
});

declare module "@tanstack/react-router" {
  interface Register {
    router: typeof router;
  }
}

const rootElement = document.getElementById("app")!;
if (!rootElement.innerHTML) {
  const root = ReactDOM.createRoot(rootElement);
  root.render(
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>,
  );
}
