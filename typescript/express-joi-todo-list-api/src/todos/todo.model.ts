export type TodoState = "open" | "done";

export type CreateTodoRequest = {
  title: string;
  description: string;
  state?: TodoState;
};

export type UpdateTodoRequest = {
  title?: string;
  description?: string;
  state?: TodoState;
};

export type TodoResponse = {
  id: number;
  title: string;
  description: string;
  state: TodoState;
};
