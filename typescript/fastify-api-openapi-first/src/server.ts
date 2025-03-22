import dotenv from 'dotenv';
import Fastify from 'fastify';
import openapiGlue from 'fastify-openapi-glue';

import { routeHandlers } from './todo.controller';

dotenv.config();

const port = parseInt(process.env.PORT ?? "8000");

// See https://fastify.dev

const fastify = Fastify({
  logger: true,
  // See https://fastify.dev/docs/latest/Reference/Validation-and-Serialization/#schema-validator
  ajv: { customOptions: { strict: false } },
});

// See https://github.com/hey-api/openapi-ts/tree/main/examples/openapi-ts-fastify
// See https://github.com/seriousme/fastify-openapi-glue

fastify.register(openapiGlue, {
  specification: "openapi.yaml",
  serviceHandlers: routeHandlers,
});

fastify.listen({ port });
