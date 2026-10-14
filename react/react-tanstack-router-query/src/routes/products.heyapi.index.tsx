import { useQuery } from "@tanstack/react-query";
import { createFileRoute } from "@tanstack/react-router";
import { query } from "../lib/api/client";

// https://heyapi.dev/docs/openapi/typescript/plugins/tanstack-query
const productsQuery = query.getAllProductsOptions({ query: { limit: 10 } });

export const Route = createFileRoute("/products/heyapi/")({
  // This ensures the query is executed and the cache is populated before the router switches to
  // this route. https://tanstack.com/router/latest/docs/guide/data-loading
  loader: ({ context }) => context.queryClient.query({ ...productsQuery, staleTime: "static" }),
  // On the first page visit, the user has to wait for the data; on later visits, cached results
  // appear immediately. The query still runs again in the background, and the component re-renders
  // when it completes with the latest data (stale-while-revalidate).
  component: ProductsPage,
});

function ProductsPage() {
  // This reads the data from the cache and subscribes to updates.
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
