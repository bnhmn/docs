import { defaultEndpointsFactory, DependsOnMethod } from "express-zod-api";
import createError from "http-errors";

import {
  CreateTodoRequest,
  DeleteTodoRequest,
  DeleteTodoResponse,
  ReadTodoRequest,
  TodoResponse,
  TodosResponse,
  UpdateTodoRequest,
} from "./todo.api";
import * as service from "./todo.service";

export const todosEndpoint = new DependsOnMethod({
  post: defaultEndpointsFactory.build({
    input: CreateTodoRequest,
    output: TodoResponse,
    handler: async ({ input }) => {
      const todo = service.createTodo(input);
      return todo;
    },
  }),
  get: defaultEndpointsFactory.build({
    output: TodosResponse,
    handler: async ({}) => {
      const todos = service.fetchTodos();
      return { todos };
    },
  }),
});

export const todoByIdEndpoint = new DependsOnMethod({
  get: defaultEndpointsFactory.build({
    input: ReadTodoRequest,
    output: TodoResponse,
    handler: async ({ input }) => {
      const todo = service.fetchTodoById(input.id);
      if (!todo) {
        throw createError.NotFound(`Todo '${input.id}' does not exist`);
      }
      return todo;
    },
  }),
  patch: defaultEndpointsFactory.build({
    input: UpdateTodoRequest,
    output: TodoResponse,
    handler: async ({ input }) => {
      const { id, ...update } = input;
      const todo = service.updateTodoById(id, update);
      if (!todo) {
        throw createError.NotFound(`Todo '${input.id}' does not exist`);
      }
      return todo;
    },
  }),
  delete: defaultEndpointsFactory.build({
    input: DeleteTodoRequest,
    output: DeleteTodoResponse,
    handler: async ({ input }) => {
      const deleted = service.deleteTodoById(input.id);
      if (!deleted) {
        throw createError.NotFound(`Todo '${input.id}' does not exist`);
      }
      return { success: true };
    },
  }),
});
