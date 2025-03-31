import express from "express";

import { optionalAuth } from "../auth";
import { Joi, validate } from "../validation";
import * as service from "./todo.service";

// See https://expressjs.com/en/guide/routing.html
export const todoRouter = express.Router();

todoRouter.post(
  "/",
  optionalAuth,
  validate({
    body: Joi.object({
      title: Joi.string().trim().required(),
      description: Joi.string().trim().required(),
      state: Joi.string().valid("open", "done").optional().default("open"),
    }),
  }),
  async (req, res) => {
    const todo = service.createTodo(req.body);
    res.status(201).set("location", `/todos/${todo.id}`).json(todo);
  },
);

todoRouter.get("/", optionalAuth, async (req, res) => {
  const todos = service.fetchTodos();
  res.json(todos);
});

todoRouter.get("/:id", optionalAuth, async (req, res) => {
  const todoId = parseInt(req.params.id);
  const todo = service.fetchTodoById(todoId);
  if (todo) {
    res.status(200).json(todo);
  } else {
    res.status(404).send();
  }
});

todoRouter.patch(
  "/:id",
  optionalAuth,
  validate({
    body: Joi.object({
      title: Joi.string().trim().optional(),
      description: Joi.string().trim().optional(),
      state: Joi.string().valid("open", "done").optional(),
    }),
  }),
  async (req, res) => {
    const todoId = parseInt(req.params.id);
    const updatedTodo = service.updateTodoById(todoId, req.body);
    if (updatedTodo) {
      res.status(200).json(updatedTodo);
    } else {
      res.status(404).send();
    }
  },
);

todoRouter.delete("/:id", optionalAuth, async (req, res) => {
  const todoId = parseInt(req.params.id);
  const deleted = service.deleteTodoById(todoId);
  if (deleted) {
    res.status(204).send();
  } else {
    res.status(404).send();
  }
});
