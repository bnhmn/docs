package com.example.webapp.security;

import java.util.Base64;

import jakarta.servlet.http.Cookie;

import com.example.webapp.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.test.web.servlet.MockMvc;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@IntegrationTest
@SuppressWarnings({"rawtypes", "unchecked"})
class FrontChannelLogoutTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  SessionRepository sessionRepository;

  @Test
  void frontChannelLogoutInvalidatesSessionAndReturns200() throws Exception {
    var session = sessionRepository.createSession();
    sessionRepository.save(session);

    mockMvc
        .perform(get("/logout/oauth2/idp").cookie(createSessionCookie(session)))
        .andExpect(status().isOk());

    assertThat(sessionRepository.findById(session.getId())).isNull();
  }

  @Test
  void frontChannelLogoutWorksWithoutSession() throws Exception {
    mockMvc.perform(get("/logout/oauth2/idp")).andExpect(status().isOk());
  }

  private Cookie createSessionCookie(Session session) {
    return new Cookie(
        "SESSION", Base64.getEncoder().encodeToString(session.getId().getBytes(UTF_8)));
  }
}
