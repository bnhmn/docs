package com.example.webapp.config;

import com.example.webapp.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.http.MediaType.TEXT_HTML;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@IntegrationTest
class SpaResourceResolverTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void forwardsRootToIndex() throws Exception {
    mockMvc
        .perform(get("/").with(oidcLogin()))
        .andExpect(status().isOk())
        .andExpect(forwardedUrl("index.html"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"/index.html", "/about", "/other"})
  void servesIndexForSpaRoutes(String spaRoute) throws Exception {
    mockMvc
        .perform(get(spaRoute).with(oidcLogin()).accept(TEXT_HTML))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("<h1>SPA test page</h1>")));
  }

  @ParameterizedTest
  @ValueSource(strings = {"/actuator/health", "/login", "/logout"})
  void doesNotServeIndexForBffRoutes(String bffRoute) throws Exception {
    mockMvc
        .perform(get(bffRoute).with(oidcLogin()))
        .andExpect(status().isOk())
        .andExpect(content().string(not(containsString("<h1>SPA test page</h1>"))));
  }
}
