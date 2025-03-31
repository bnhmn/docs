import { CreateTodoRequest, TodoResponse, UpdateTodoRequest } from "./generated";

const todos: TodoResponse[] = [
  { id: 1, title: "Buy flowers", description: "Buy some flowers", state: "open" },
  { id: 2, title: "Cook dinner", description: "Cook some dinner", state: "open" },
  { id: 3, title: "Clean the bathroom", description: "Clean the bathroom", state: "open" },
];
let next_todo_id = todos.length + 1;

export function createTodo(request: CreateTodoRequest): TodoResponse {
  const todo = {
    id: next_todo_id++,
    title: request.title,
    description: request.description,
    state: request.state ?? "open",
  };
  todos.push(todo);
  return todo;
}

export function findAllTodos(): TodoResponse[] {
  return todos;
}

export function findTodoById(id: number): TodoResponse | undefined {
  return todos.find((todo) => todo.id === id);
}

export function updateTodoById(id: number, request: UpdateTodoRequest): TodoResponse | undefined {
  const todo = findTodoById(id);
  if (todo) {
    todo.title = request.title ?? todo.title;
    todo.description = request.description ?? todo.description;
    todo.state = request.state ?? todo.state;
  }
  return todo;
}

export function deleteTodoById(id: number): boolean {
  const todoIndex = todos.findIndex((todo) => todo.id === id);
  todos.splice(todoIndex, 1);
  return todoIndex >= 0;
}
