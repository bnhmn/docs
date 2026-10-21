# React with Tanstack Router

[TanStack Router](https://tanstack.com/router/latest/docs/quick-start) is a React routing library
with file-based routes and type-safe navigation. It maps URLs to pages and can load data before a
route is rendered.

[TanStack Query](https://tanstack.com/query/latest/docs/framework/react/overview) manages server
state: it fetches backend data, caches it, and refreshes it in the background.

This guide shows how to set up a React app with Router and how Query can complement its route
loaders.

## 1. Create a Tanstack Router project

Run the following command:

```bash
npx @tanstack/cli create --router-only
```

It will output something like this:

```bash
◇  Select framework: » React
◇  Project name:     » react-tanstack-router-query
◇  Select toolchain: » Biome
◇  Would you like to initialize a new git repository? » No
◇  Continue with these settings? » Yes
```

To run the application:

```bash
npm run dev
```

To build the application for production:

```bash
npm run build
```

## 2. Formatting and linting with Biome

This project uses [Biome](https://biomejs.dev/) for linting and formatting.

Replace the three existing scripts with these two in `package.json`:

```diff
{
  "scripts": {
    "fix": "biome check --write",
    "ci:check": "biome ci"
-    "format": "biome format",
-    "lint": "biome lint",
-    "check": "biome check"
  }
}
```

Use this project's [Biome preset](biome.json) or configure it yourself.

Run `npm run fix` to format files and apply safe fixes. Run `npm run ci:check` to check the project
without modifying files.

## 3. Styling

This project uses [Tailwind CSS](https://tailwindcss.com/) for styling.

If you prefer not to use Tailwind CSS:

1. Replace the Tailwind import in `src/styles.css` with your own styles
2. Remove `tailwindcss()` from the plugins array in `vite.config.ts`
3. Remove `@tailwindcss/vite` and `tailwindcss` from `package.json`

## 4. Routing

Routes are files in the `src/routes` folder; the router plugin generates the route tree from them.
Add a file such as `src/routes/about.tsx` to create an `/about` route:

```tsx
import { createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/about")({
  head: () => ({
    meta: [{ title: "About" }],
  }),
  component: AboutPage,
});

// The component must be defined as an extra function for Hot Module Reload (HMR) to work.
function AboutPage() {
  return <h1>About</h1>;
}
```

Use `Link` for client-side navigation between routes:

```tsx
import { Link } from "@tanstack/react-router";

function Navigation() {
  return <Link to="/about">About</Link>;
}
```

The root route in `src/routes/__root.tsx` is shared by every page. Put common UI, such as a header
or navigation, there and render the active page with `<Outlet />`. See the
[Routing Concepts](https://tanstack.com/router/latest/docs/routing/routing-concepts) for layouts and
other route features.

## 5. Data Fetching with Tanstack Query

Install TanStack Query:

```bash
npm i @tanstack/react-query
```

Create a `QueryClient` and provide it to the app in `src/main.tsx`, so other route loaders can
access it:

```tsx
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { RouterProvider, createRouter } from "@tanstack/react-router";
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
```

Type the context in the `src/routes/__root.tsx` route:

```tsx
import type { QueryClient } from "@tanstack/react-query";
import { Outlet, createRootRouteWithContext } from "@tanstack/react-router";

import "../styles.css";

// The root route is the top-most route and encapsulates all other routes as children.
// Put common UI here, such as a header or navigation.
// https://tanstack.com/router/latest/docs/routing/routing-concepts#the-root-route

export const Route = createRootRouteWithContext<{ queryClient: QueryClient }>()({
  component: RootComponent,
});

function RootComponent() {
  return (
    <>
      {/* Outlet is the place where Tanstack Router inserts the active page */}
      <Outlet />
    </>
  );
}
```

Define a query once, then use it in both the route loader and component. This example fetches
products from the [DummyJSON API](https://dummyjson.com/openapi.json), in `src/routes/products.tsx`:

```tsx
import { queryOptions, useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";

type Product = { id: number; title: string };
type ProductList = { products: Product[] };

const productsQuery = queryOptions({
  // The queryKey identifies cached data. Invalidating a key marks queries that contain this key as
  // stale; they refetch automatically the next time their component becomes visible.
  queryKey: ["products"],
  queryFn: async () => {
    const response = await fetch("https://dummyjson.com/products?limit=10");
    if (!response.ok) throw new Error("Could not load products");
    return (await response.json()) as ProductList;
  },
});

export const Route = createFileRoute("/products")({
  // This ensures the query is executed and the cache is populated before the router switches to
  // this route. https://tanstack.com/router/latest/docs/guide/data-loading
  loader: ({ context }) => context.queryClient.query({ ...productsQuery, staleTime: "static" }),
  component: ProductsPage,
});

function ProductsPage() {
  // This reads the data from the cache and subscribes to updates.
  const { data } = useQuery(productsQuery);
  return (
    <>
      <h1 className="text-3xl">Products</h1>
      <ul>
        {data?.products.map((product) => (
          <li key={product.id}>{product.title}</li>
        ))}
      </ul>
    </>
  );
}
```

The loader ensures data is in Query's cache before the route renders. On the first page visit, the
users has to wait for the data. On later visits, cached results appear immediately. The query still
runs again in the background, and the component re-renders when it completes with the latest data
(stale-while-revalidate).

See [Query integration guide](https://tanstack.com/router/latest/docs/integrations/query) for more
options.

## 5. Type-safe data fetching with Hey API

[Hey API](https://heyapi.dev/) generates a typed API client, TypeScript types, and TanStack Query
options from an OpenAPI specification such as <https://dummyjson.com/openapi.json>.

### Enable code generation

Install Hey API and its Vite plugin:

```bash
npm install -D -E @hey-api/openapi-ts @hey-api/vite-plugin
```

Add `heyApiPlugin` to the plugins in `vite.config.ts`:

```ts
import { heyApiPlugin } from "@hey-api/vite-plugin";

export default defineConfig({
  plugins: [
    // https://heyapi.dev/docs/openapi/typescript/configuration/vite
    heyApiPlugin({
      config: {
        input: "https://dummyjson.com/openapi.json",
        output: "src/lib/api/generated",
        plugins: ["@hey-api/client-fetch", { name: "@tanstack/react-query", queryOptions: true }],
      },
    }),
  ],
});
```

Extend your `.gitignore` file:

```gitignore
generated
*.gen.ts
```

The plugin runs when Vite loads its config, so `npm run dev` and `npm run build` generate the client
automatically. No separate code-generation command is needed.

### Configure the client

Set the API URL in Vite env files, for example in `.env.development` and `.env.production`:

```env
VITE_API_BASE_URL=https://dummyjson.com
```

Configure the fetch client in `src/api/client.ts`:

```ts
import { createClient, createConfig, type ClientOptions } from "./generated/client";
export * as query from "./generated/@tanstack/react-query.gen";
export * as api from "./generated/sdk.gen";
export * from "./generated/types.gen";

// This is a type-safe REST client automatically generated from the backend's OpenAPI specification
// https://heyapi.dev/docs/openapi/typescript/clients/fetch

export const client = createClient(
  createConfig<ClientOptions>({
    baseUrl: import.meta.env.VITE_API_BASE_URL,
  }),
);
```

### Use generated queries

Hey API [generates query options](https://heyapi.dev/docs/openapi/typescript/plugins/tanstack-query)
from operation IDs. DummyJSON's products endpoint becomes `getAllProductsOptions`. Reuse the
generated options in the route loader and component:

```tsx
import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { query } from "../lib/api/client";

const productsQuery = query.getAllProductsOptions({ query: { limit: 10 } });

export const Route = createFileRoute("/products/heyapi/")({
  loader: ({ context }) => context.queryClient.query({ ...productsQuery, staleTime: "static" }),
  component: ProductsPage,
});

function ProductsPage() {
  const { data } = useQuery(productsQuery);
  return (
    <>
      <h1 className="text-3xl">Products</h1>
      <ul>
        {data?.products?.map((product) => (
          <li key={product.id}>{product.title}</li>
        ))}
      </ul>
    </>
  );
}
```

## 6. Recommended Folder Structure

Group code by feature as the application grows. Feature components and queries stay together; shared
UI and API infrastructure belong in common folders.

```text
src/
├─ routes/              # Map URLs to pages
├─ features/products/
│  ├─ components/       # Product-specific UI
│  └─ queries.ts        # Product queries and mutations
├─ shared/
│  ├─ components/       # App-wide UI, such as navigation
│  ├─ lib/              # Generic helpers, such as formatters
│  └─ api/              # API client and generated types
└─ tests/
   ├─ products/
   │  ├─ components/    # Product component tests
   │  └─ support/       # Product test utilities (fixtures, mocks, etc.)
   └─ e2e/              # Browser-level end-to-end tests
```

1. Route files connect URLs to pages, and the `__root.tsx` route defines the root layout.
2. Put app-wide UI, such as navigation, in `shared/components` and render it from the root layout.
3. Components inside `features/products` render product-specific UI.
4. Put generic helpers such as date or currency formatters in `shared/lib`.
5. Keep feature-specific helpers inside their feature.
6. Keep tests in the top-level `tests` folder and mirror the feature structure. This cleanly
   separates source and test code.

## 7. Testing

### React test basics

The [React component testing guide](../react-fundamentals/README.md#4-test-react-components) covers
the general test setup and recommended testing patterns.

### Testing TanStack Query

Components that use `useQuery` need a `QueryClientProvider`. Create a fresh `QueryClient` for each
test with seeded data, so the component doesn't need to make a network request (if you need to test
data fetching, error, retry, or refetch behavior, mock the API instead).

```tsx
// tests/support/render-with-products.tsx
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { render } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import type { ReactElement } from "react";
import type { Product } from "../../src/...";

export function renderWithProducts(ui: ReactElement, products: Product[]) {
  const queryClient = new QueryClient({
    // Disable retries so tests fail fast, and use infinite staleTime to prevent refetching
    defaultOptions: { queries: { retry: false, staleTime: Infinity } },
  });
  queryClient.setQueryData(["products"], { products });

  render(<QueryClientProvider client={queryClient}>{ui}</QueryClientProvider>);
  return { user: userEvent.setup() };
}
```

### Testing TanStack Router

Components that use router hooks or links must render inside a `RouterProvider`. Use
`createMemoryHistory` to start at a chosen path without relying on the browser URL. Apply the
generated route tree and provide the `QueryClient` to both the router and query context, so tests
can cover the loader and navigation.

```tsx
// tests/support/render-products-page.tsx
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { createMemoryHistory, createRouter, RouterProvider } from "@tanstack/react-router";
import { render } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { routeTree } from "../../src/routeTree.gen";
import type { Product } from "../../src/...";

export function renderProductsPage(products: Product[]) {
  const queryClient = new QueryClient({
    // Disable retries so tests fail fast, and use infinite staleTime to prevent refetching
    defaultOptions: { queries: { retry: false, staleTime: Infinity } },
  });
  queryClient.setQueryData(["products"], { products });

  const router = createRouter({
    routeTree,
    context: { queryClient },
    history: createMemoryHistory({ initialEntries: ["/products"] }),
  });

  render(
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>,
  );
  return { router, user: userEvent.setup(), queryClient };
}
```

Await route content with `findBy...` queries when loading is asynchronous.

For an isolated component, you don't need to provide the full route tree; a small test router with
only the needed route is enough.

## 8. Learn More

You can learn more about all of the offerings from TanStack in the
[TanStack documentation](https://tanstack.com).

See also [React - Recommended Tools](../react-fundamentals/README.md#5-recommended-tools).
