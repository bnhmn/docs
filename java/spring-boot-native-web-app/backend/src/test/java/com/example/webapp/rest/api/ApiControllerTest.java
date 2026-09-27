package com.example.webapp.rest.api;

import com.example.webapp.IntegrationTest;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.oauth2.client.ClientAuthorizationRequiredException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@IntegrationTest
@WireMockTest(httpPort = 12345)
@ExtendWith(MockitoExtension.class)
class ApiControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void forwardsGetRequestToBackendAndReturnsResponse() throws Exception {
    stubFor(get(urlEqualTo("/api/todos?state=open")).willReturn(okJson("[]")));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/todos?state=open")
            .accept(APPLICATION_JSON)
            .with(oidcLogin())
            .with(oauth2Client("idp")))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    verify(getRequestedFor(urlEqualTo("/api/todos?state=open"))
        .withHeader(AUTHORIZATION, equalTo("Bearer access-token"))
        .withHeader(ACCEPT, equalTo(APPLICATION_JSON_VALUE)));
  }

  @Test
  void forwardsPostRequestToBackendAndReturnsResponse() throws Exception {
    var requestBody = """
        {"title": "Clean the bathroom", "state": "open"}
        """;
    var responseBody = """
        {"id": 1, "title": "Clean the bathroom", "state": "open"}
        """;

    stubFor(post(urlEqualTo("/api/todos"))
        .willReturn(aResponse()
            .withStatus(201)
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody(responseBody)));

    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/todos")
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .content(requestBody)
            .with(oidcLogin())
            .with(oauth2Client("idp"))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(content().json(responseBody));

    verify(postRequestedFor(urlEqualTo("/api/todos"))
        .withHeader(AUTHORIZATION, equalTo("Bearer access-token"))
        .withHeader(ACCEPT, equalTo(APPLICATION_JSON_VALUE))
        .withHeader(CONTENT_TYPE, containing(APPLICATION_JSON_VALUE))
        .withRequestBody(equalToJson(requestBody)));
  }

  @ParameterizedTest
  @ValueSource(ints = {404, 500})
  void preservesBackendErrorResponse(int backendStatus) throws Exception {
    var responseBody = """
        {"error": "ToDo not found"}
        """;

    stubFor(get(urlEqualTo("/api/todos/1234"))
        .willReturn(aResponse()
            .withStatus(backendStatus)
            .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .withBody(responseBody)));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/todos/1234")
            .accept(APPLICATION_JSON)
            .with(oidcLogin())
            .with(oauth2Client("idp")))
        .andExpect(status().is(backendStatus))
        .andExpect(content().json(responseBody));
  }

  @Test
  void invalidatesSessionAndReturns401WhenClientAuthorizationIsRequired(@Mock RestClient restClient)
      throws Exception {
    // Covers the case where access and refresh tokens have expired; the user needs to log in again.
    var mockMvc = standaloneSetup(new ApiController(restClient)).build();
    var session = new MockHttpSession();
    Mockito.when(restClient.method(GET)).thenThrow(new ClientAuthorizationRequiredException("idp"));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/todos/1234").session(session))
        .andExpect(status().isUnauthorized());

    assertThat(session.isInvalid()).isTrue();
  }
}
