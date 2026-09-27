package com.example.webapp.security;

import com.example.webapp.IntegrationTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@IntegrationTest
public class SecurityConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @ParameterizedTest
  @ValueSource(strings = {"/", "/index.html", "/api/me"})
  void authenticatedUserCanAccessProtectedEndpoints(String endpoint) throws Exception {
    mockMvc.perform(get(endpoint).with(oidcLogin())).andExpect(status().isOk());
  }

  @ParameterizedTest
  @ValueSource(strings = {"/", "/index.html", "/api/me"})
  void unauthenticatedUserCannotAccessProtectedEndpoints(String endpoint) throws Exception {
    mockMvc
        .perform(get(endpoint))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string(LOCATION, "/oauth2/authorization/idp"));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"/actuator/health", "/actuator/health/readiness", "/actuator/health/liveness"})
  void unauthenticatedUserCanAccessHealthEndpoints(String endpoint) throws Exception {
    mockMvc.perform(get(endpoint)).andExpect(status().isOk());
  }
}
