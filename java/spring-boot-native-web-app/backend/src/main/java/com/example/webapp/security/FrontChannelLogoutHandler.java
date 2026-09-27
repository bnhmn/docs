package com.example.webapp.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FrontChannelLogoutHandler {

  private final LogoutHandler logoutHandler = new SecurityContextLogoutHandler();

  /**
   * The identity provider calls this endpoint after the user has signed out of another application,
   * enabling us to sign them out of this application as well ("single sign-out"). It ensures that
   * the user's local session is cleared rather than waiting for the access token to expire. This
   * mechanism is also known as "OIDC Front-Channel Logout" and only works if it is supported by
   * your identity provider and configured accordingly. Note that the endpoint is deliberately
   * CSRF-exposed.
   */
  @GetMapping("/logout/oauth2/idp")
  void logout(HttpServletRequest req, HttpServletResponse res, Authentication authentication) {
    logoutHandler.logout(req, res, authentication);
  }
}
