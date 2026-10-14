import { describe, expect, test } from "vitest";

import { createTodo, deleteTodoById, fetchTodoById, fetchTodos, updateTodoById } from "../src/generated/sdk.gen";
import { client } from "./utils/client";

describe("TODO", () => {
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

  test("the client can fetch a todo", async () => {
    const { response, data } = await fetchTodoById({
      client,
      path: { id: 1 },
    });

    expect(response.status).toBe(200);
    expect(data?.id).toBeDefined();
    expect(data?.title).toBe("Buy flowers");
    expect(data?.description).toBe("Buy some flowers");
    expect(data?.state).toBe("open");
  });

  test("the client can update a todo", async () => {
    const { response, data } = await updateTodoById({
      client,
      path: { id: 1 },
      body: {
        title: "Buy fancy flowers",
        description: "Buy some fancy flowers",
        state: "done",
      },
    });

    expect(response.status).toBe(200);
    expect(data?.id).toBeDefined();
    expect(data?.title).toBe("Buy fancy flowers");
    expect(data?.description).toBe("Buy some fancy flowers");
    expect(data?.state).toBe("done");
  });

  test("the client can delete a todo", async () => {
    const { response } = await deleteTodoById({
      client,
      path: { id: 1 },
    });

    expect(response.status).toBe(204);
  });

  test("the client can fetch all todos", async () => {
    const { response, data } = await fetchTodos({
      client,
    });

    expect(response.status).toBe(200);
    expect(data).toStrictEqual([
      {
        id: 2,
        title: "Cook dinner",
        description: "Cook some dinner",
        state: "open",
      },
      {
        id: 3,
        title: "Clean the bathroom",
        description: "Clean the bathroom",
        state: "open",
      },
      {
        id: 4,
        title: "Buy milk",
        description: "Buy some milk from the supermarket",
        state: "open",
      },
    ]);
  });
});
