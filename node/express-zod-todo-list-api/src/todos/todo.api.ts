import { z } from "zod";

export const CreateTodoRequest = z.object({
  title: z.string().trim(),
  description: z.string().trim(),
  state: z.enum(["open", "done"]).optional().default("open"),
});

export const ReadTodoRequest = z.object({
  id: z.coerce.number().positive(),
});

export const DeleteTodoRequest = z.object({
  id: z.coerce.number().positive(),
});

export const UpdateTodoRequest = z.object({
  id: z.coerce.number().positive(),
  title: z.string().trim().optional(),
  description: z.string().trim().optional(),
  state: z.enum(["open", "done"]).optional(),
});

export const TodoResponse = z.object({
  id: z.number().int(),
  title: z.string(),
  description: z.string(),
  state: z.enum(["open", "done"]),
});

export const TodosResponse = z.object({
  todos: z.array(TodoResponse),
});

export const DeleteTodoResponse = z.object({
  success: z.boolean(),
});
