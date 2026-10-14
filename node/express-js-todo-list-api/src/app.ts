import "express-async-errors";

import cors from "cors";
import express from "express";
import morgan from "morgan";

import { authErrorHandler } from "./auth";
import { todoRouter } from "./todos/todo.controller";
import { internalServerErrorHandler, validationErrorHandler } from "./validation";

const app = express();
const port = parseInt(process.env.PORT || "8000");

app.use(cors());
app.use(express.static("public")); // Serve frontend files from public folder
app.use(morgan("short")); // request logging https://www.npmjs.com/package/morgan
app.use(express.json());

app.use("/todos", todoRouter);

app.use(authErrorHandler);
app.use(validationErrorHandler);
app.use(internalServerErrorHandler);

app.listen(port, () => {
  console.log(`Server is listening on http://localhost:${port}`);
});
