import { Todo, TodoCreate, TodoUpdate } from "./todo.model";

const todos: Todo[] = [
  { id: 1, title: "Buy flowers", description: "Buy some flowers", state: "open" },
  { id: 2, title: "Cook dinner", description: "Cook some dinner", state: "open" },
  { id: 3, title: "Clean the bathroom", description: "Clean the bathroom", state: "open" },
];
let next_todo_id = todos.length + 1;

export function createTodo(request: TodoCreate): Todo {
  const todo = {
    id: next_todo_id++,
    title: request.title,
    description: request.description,
    state: request.state,
  };
  todos.push(todo);
  return todo;
}

export function findAllTodos(): Todo[] {
  return todos;
}

export function findTodoById(id: number): Todo | undefined {
  return todos.find((todo) => todo.id === id);
}

export function updateTodoById(id: number, request: TodoUpdate): Todo | undefined {
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
  if (todoIndex >= 0) {
    todos.splice(todoIndex, 1);
  }
  return todoIndex >= 0;
}
