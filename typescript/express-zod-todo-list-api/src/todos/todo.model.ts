export type TodoState = "open" | "done";

export type TodoCreate = {
  title: string;
  description: string;
  state: TodoState;
};

export type TodoUpdate = {
  title?: string;
  description?: string;
  state?: TodoState;
};

export type Todo = {
  id: number;
  title: string;
  description: string;
  state: TodoState;
};
