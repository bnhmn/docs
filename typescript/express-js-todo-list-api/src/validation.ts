import { ErrorRequestHandler } from "express";
import { validate as joiValidate, schema, ValidationError } from "express-validation";

export { Joi } from "express-validation";

/**
 * Validate that the request body matches given schema.
 * On validation errors, an error message will be returned to the client.
 */
export function validate(schema: schema) {
  return joiValidate(
    schema,
    { context: true, keyByField: true },
    { abortEarly: false, allowUnknown: true, stripUnknown: true },
  );
}

/**
 * Converts validation error into client response.
 */
export const validationErrorHandler: ErrorRequestHandler = (error, req, res, next) => {
  if (error instanceof ValidationError) {
    res.status(400).json(error);
  } else {
    next(error);
  }
};

export const internalServerErrorHandler: ErrorRequestHandler = (error, req, res, next) => {
  console.error(error);
  res.status(500).json({ code: "internal_server_error" });
};
