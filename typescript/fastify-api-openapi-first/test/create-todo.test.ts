import { describe, expect, test } from "vitest";

import { createTodo } from "./generated/sdk.gen";
import { client } from "./utils/client";

describe("Create TODO", () => {
  test("the client can create a todo", async () => {
    const { data, response } = await createTodo({
      client,
      body: {
        title: "Buy milk",
        description: "Buy some milk from the supermarket",
      },
    });

    expect(response.status).toBe(201);
    expect(data?.id).toBeDefined();
    expect(data?.title).toBe("Buy milk");
    expect(data?.description).toBe("Buy some milk from the supermarket");
    expect(data?.state).toBe("open");
  });
});
