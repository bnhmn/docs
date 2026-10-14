import { createConfig, createServer, Routing } from "express-zod-api";

import { todoByIdEndpoint, todosEndpoint } from "./todos/todo.controller";

const port = parseInt(process.env.PORT || "8000");

// express-zod-api is a wrapper around Express and Zod that helps you build structured and type-safe APIs.
// See https://github.com/RobinTail/express-zod-api https://zod.dev/

const config = createConfig({
  http: {
    listen: port,
  },
  cors: true,
});

const routing: Routing = {
  todos: todosEndpoint.nest({
    ":id": todoByIdEndpoint,
  }),
};

createServer(config, routing);
