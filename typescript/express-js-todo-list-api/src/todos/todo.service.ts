import { CreateTodoRequest, TodoResponse, UpdateTodoRequest } from "./todo.model";
import * as repository from "./todo.repository";

export function createTodo(request: CreateTodoRequest): TodoResponse {
  return repository.createTodo(request);
}

export function fetchTodos(): TodoResponse[] {
  return repository.findAllTodos();
}

export function fetchTodoById(id: number): TodoResponse | undefined {
  return repository.findTodoById(id);
}

export function updateTodoById(id: number, request: UpdateTodoRequest): TodoResponse | undefined {
  return repository.updateTodoById(id, request);
}

export function deleteTodoById(id: number): boolean {
  return repository.deleteTodoById(id);
}
