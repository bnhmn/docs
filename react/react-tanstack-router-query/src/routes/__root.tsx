import { TanStackDevtools } from "@tanstack/react-devtools";
import type { QueryClient } from "@tanstack/react-query";
import { createRootRouteWithContext, Outlet } from "@tanstack/react-router";
import { TanStackRouterDevtoolsPanel } from "@tanstack/react-router-devtools";

import "../styles.css";

// The root route is the top-most route in the entire tree and encapsulates all other routes as children.
// It has no path, it is always matched, and its component is always rendered.
// https://tanstack.com/router/latest/docs/routing/routing-concepts#the-root-route

export const Route = createRootRouteWithContext<{ queryClient: QueryClient }>()({
  component: RootComponent,
});

function RootComponent() {
  return (
    <>
      {/* Outlet is the place where Tanstack Router inserts the child content */}
      <Outlet />
      <TanStackDevtools
        config={{ position: "bottom-right" }}
        plugins={[{ name: "TanStack Router", render: <TanStackRouterDevtoolsPanel /> }]}
      />
    </>
  );
}
