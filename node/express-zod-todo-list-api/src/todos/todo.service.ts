import { Todo, TodoCreate, TodoUpdate } from "./todo.model";
import * as repository from "./todo.repository";

export function createTodo(request: TodoCreate): Todo {
  return repository.createTodo(request);
}

export function fetchTodos(): Todo[] {
  return repository.findAllTodos();
}

export function fetchTodoById(id: number): Todo | undefined {
  return repository.findTodoById(id);
}

export function updateTodoById(id: number, request: TodoUpdate): Todo | undefined {
  return repository.updateTodoById(id, request);
}

export function deleteTodoById(id: number): boolean {
  return repository.deleteTodoById(id);
}
