import { ErrorRequestHandler, RequestHandler } from "express";
import { auth, UnauthorizedError } from "express-oauth2-jwt-bearer";

// There are two express jwt middlewares. We are using the latter one:
// https://www.npmjs.com/package/express-jwt (old one by auth0, but still supported)
// https://www.npmjs.com/package/express-oauth2-jwt-bearer (new one by auth0, has a simpler api)
// When using Auth0 as identity provider, see also: https://auth0.com/docs/quickstart/backend/nodejs/interactive.

/**
 * The Authentication middleware.
 * When used, it checks that the request has a valid JWT Access Token signed by the configured issuer.
 * If the token is not valid, the user gets a 401 Authentication error when they try to access the endpoint.
 * If the token is valid, the JWT payload can be accessed at req.auth.payload in the request handler.
 */
export const requiresAuth = auth({
  audience: "<the-audience>",
  issuerBaseURL: "<the-issuer-base-url>",
  tokenSigningAlg: "RS256",
});

export const optionalAuth = auth({
  audience: "<the-audience>",
  issuerBaseURL: "<the-issuer-base-url>",
  tokenSigningAlg: "RS256",
  authRequired: false,
});

/**
 * The Authorization middleware (to be placed after the authentication middleware).
 * When used, it checks checks that the user has the admin permission.
 * If the permission is missing, the user will receive a 403 Authorization error and cannot access the endpoint.
 */
export const requiresAdminPermission: RequestHandler = (req, res, next) => {
  const permissions = (req.auth?.payload.permissions as string[]) ?? [];
  if (permissions.includes("admin")) {
    next();
  } else {
    res.status(403).json({ code: "insufficient_permissions" });
  }
};

/**
 * Converts auth errors into client response.
 */
export const authErrorHandler: ErrorRequestHandler = (error, req, res, next) => {
  if (error instanceof UnauthorizedError) {
    res.status(error.status).json({ code: "code" in error ? error.code : "invalid_request" });
  } else {
    next(error);
  }
};
