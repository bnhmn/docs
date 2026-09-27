package com.example.webapp.security;

import com.example.webapp.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@IntegrationTest
public class SecurityConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @ParameterizedTest
  @ValueSource(
      strings = {"/actuator/health", "/actuator/health/readiness", "/actuator/health/liveness"})
  void healthEndpointsArePublic(String endpoint) throws Exception {
    mockMvc.perform(get(endpoint)).andExpect(status().isOk());
  }

  @ParameterizedTest
  @ValueSource(strings = {"/", "/index.html", "/api/me"})
  void authenticatedUserCanAccessProtectedEndpoints(String endpoint) throws Exception {
    mockMvc.perform(get(endpoint).with(oidcLogin())).andExpect(status().isOk());
  }

  @ParameterizedTest
  @ValueSource(strings = {"/", "/index.html"})
  void unauthenticatedBrowserUserIsRedirectedToLoginPage(String endpoint) throws Exception {
    mockMvc
        .perform(get(endpoint).header(ACCEPT, TEXT_HTML_VALUE))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string(LOCATION, "/oauth2/authorization/idp"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"/api/me"})
  void unauthenticatedApiRequestIsRejected(String endpoint) throws Exception {
    mockMvc.perform(get(endpoint)).andExpect(status().isUnauthorized());
  }

  @Test
  void postApiRequestWithoutCsrfTokenIsRejected() throws Exception {
    mockMvc.perform(post("/api/foo").with(oidcLogin())).andExpect(status().isForbidden());
  }

  @Test
  void logoutRedirectsToIdpEndSessionEndpoint(
      @Autowired ClientRegistrationRepository clientRegistrationRepository) throws Exception {
    var clientRegistration = clientRegistrationRepository.findByRegistrationId("idp");
    mockMvc
        .perform(post("/logout")
            .with(oidcLogin().clientRegistration(requireNonNull(clientRegistration)))
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(
            redirectedUrlPattern(
                "http://localhost:3000/default/endsession?id_token_hint=id-token&post_logout_redirect_uri=http://localhost/login"));
  }
}
