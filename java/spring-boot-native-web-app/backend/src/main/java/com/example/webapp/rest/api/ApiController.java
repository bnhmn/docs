package com.example.webapp.rest.api;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.oauth2.client.web.ClientAttributes.clientRegistrationId;

/**
 * Proxies all requests below {@code /api/**} to the configured backend service.
 *
 * <p>Direct forwarding is sufficient while the frontend closely maps to the backend API. If
 * integration logic or frontend-specific data models are needed, dedicated BFF endpoints are a
 * better fit.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiController {

  private final RestClient apiRestClient;

  @RequestMapping("/api/**")
  void proxy(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      apiRestClient
          .method(HttpMethod.valueOf(request.getMethod()))
          .uri(
              request.getRequestURI(),
              uri -> uri.query(request.getQueryString()).build())
          // https://docs.spring.io/spring-security/reference/servlet/oauth2/client/authorized-clients.html#oauth2-client-rest-client
          .attributes(clientRegistrationId("idp"))
          .headers(headers -> {
            copyIfPresent(request, headers, ACCEPT);
            copyIfPresent(request, headers, CONTENT_TYPE);
          })
          .body(outputStream -> request.getInputStream().transferTo(outputStream))
          .exchange((_, clientResponse) -> {
            response.setStatus(clientResponse.getStatusCode().value());
            Optional.ofNullable(clientResponse.getHeaders().getContentType())
                .map(MimeType::toString)
                .ifPresent(response::setContentType);
            clientResponse.getBody().transferTo(response.getOutputStream());
            return null;
          });
    } catch (ClientAuthorizationRequiredException ex) {
      // If the user's access token is no longer valid, invalidate the stale session and return
      // 401 so the SPA can start a new login flow.
      Optional.ofNullable(request.getSession(false)).ifPresent(HttpSession::invalidate);
      response.sendError(UNAUTHORIZED.value());
    }
  }

  private static void copyIfPresent(HttpServletRequest request, HttpHeaders headers, String name) {
    String value = request.getHeader(name);
    if (value != null) {
      headers.set(name, value);
    }
  }
}
