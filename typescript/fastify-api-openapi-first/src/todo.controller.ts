import { RouteHandlers } from "./generated/fastify.gen";
import * as service from "./todo.service";

export const routeHandlers: RouteHandlers = {
  createTodo(request, reply) {
    const todo = service.createTodo(request.body);
    reply.header("location", `/todos/${todo.id}`).status(201).send(todo);
  },

  fetchTodos(request, reply) {
    const todos = service.fetchTodos();
    reply.status(200).send(todos);
  },

  fetchTodoById(request, reply) {
    const todo = service.fetchTodoById(request.params.id);
    if (todo) {
      reply.status(200).send(todo);
    } else {
      reply.status(404).send();
    }
  },

  updateTodoById(request, reply) {
    const todo = service.updateTodoById(request.params.id, request.body);
    if (todo) {
      reply.status(200).send(todo);
    } else {
      reply.status(404).send();
    }
  },

  deleteTodoById(request, reply) {
    const success = service.deleteTodoById(request.params.id);
    if (success) {
      reply.status(204).send();
    } else {
      reply.status(404).send();
    }
  },
};
