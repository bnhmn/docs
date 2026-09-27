package com.example.webapp.rest.api;

import com.example.webapp.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@IntegrationTest
class MeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void returnsUserDetails() throws Exception {
    mockMvc
        .perform(get("/api/me").with(oidcLogin()))
        .andExpect(status().isOk())
        .andExpect(content().json("""
         { "sub": "user" }"""));
  }
}
