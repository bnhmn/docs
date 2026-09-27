package com.example.webapp.rest.api;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class MeController {

  /** This endpoint returns the details of the logged-in user. */
  @GetMapping(path = "/api/me", produces = APPLICATION_JSON_VALUE)
  public Map<String, Object> me(@AuthenticationPrincipal OidcUser oidcUser) {
    return oidcUser.getClaims();
  }
}
