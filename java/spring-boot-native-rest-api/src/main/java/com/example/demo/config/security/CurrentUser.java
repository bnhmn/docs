package com.example.demo.config.security;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

/** Provides access to the currently authenticated user. */
@Service
public class CurrentUser {

  /**
   * Returns the authenticated user's ID.
   *
   * @throws NoSuchElementException if no JWT is available in the security context
   * @implNote The ID is read from the {@code sub} claim of the JWT token.
   */
  public String getUserId() {
    var auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    return Optional.ofNullable(auth)
        .map(AbstractOAuth2TokenAuthenticationToken::getToken)
        .map(JwtClaimAccessor::getSubject)
        .orElseThrow();
  }
}
