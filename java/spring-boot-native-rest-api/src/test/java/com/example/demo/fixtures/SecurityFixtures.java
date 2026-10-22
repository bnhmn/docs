package com.example.demo.fixtures;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import tools.jackson.databind.annotation.JsonNaming;

import com.example.demo.fixtures.SecurityFixtures.TokenRequest.TokenRequestBuilder;

import static java.util.Objects.requireNonNull;

/**
 * Provides authentication fixtures for integration tests.
 *
 * <p>Note that retrieving auth tokens only works within integration tests, as it requires the test
 * containers to have been started.
 */
@UtilityClass
public class SecurityFixtures {

  // spotless:off
  public static final String TEST_CLIENT_ID = "test-client-id";
  public static final String TEST_CLIENT_SECRET = "test-client-secret";
  public static final String TEST_AUDIENCE = "https://myapi.example.com";
  public static final String TEST_USER_1 = "test-user-1";
  public static final String TEST_SCOPE = "playlist";
  /** Retrieves a valid access token for integration tests. */
  public static final Supplier<String> VALID_TOKEN = () -> getToken(aTokenRequest());
  public static final Supplier<String> INVALID_TOKEN_EMPTY_STRING = () -> "";
  public static final Supplier<String> INVALID_TOKEN_WRONG_AUDIENCE = () -> getToken(aTokenRequest().audience("invalid-audience"));
  public static final Supplier<String> INVALID_TOKEN_NO_PLAYLIST_SCOPE = () -> getToken(aTokenRequest().scope(""));
  // spotless:on

  private static RestClient restClient;
  private static ObjectMapper objectMapper;
  private static ConcurrentHashMap<TokenRequest, String> cachedTokens;

  /**
   * Initializes the connection to the mock OAuth server. Only after this can tokens be retrieved.
   */
  public static void initialize(String oauthMockServerIssuerUrl) {
    restClient = RestClient.builder().baseUrl(oauthMockServerIssuerUrl).build();
    objectMapper = new ObjectMapper();
    cachedTokens = new ConcurrentHashMap<>();
  }

  /**
   * Retrieves an access token that matches the request. If a suitable token is already cached, the
   * cached token is returned. Otherwise, a new token is fetched from the mock OAuth server and
   * loaded into the cache.
   */
  private static String getToken(TokenRequestBuilder requestBuilder) {
    var request = requestBuilder.build();
    if (!cachedTokens.containsKey(request)) {
      // https://github.com/navikt/mock-oauth2-server
      var reqMap = objectMapper.convertValue(request, new TypeReference<Map<String, String>>() {});
      var response = restClient
          .post()
          .uri("/token")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body(MultiValueMap.fromSingleValue(reqMap))
          .retrieve()
          .body(TokenResponse.class);
      cachedTokens.put(request, requireNonNull(response).accessToken());
    }
    return cachedTokens.get(request);
  }

  /**
   * A request for a valid access token.
   *
   * @see {@link com.example.demo.TestContainers#mockOAuthServer}
   */
  @SuppressWarnings({"JavadocReference", "JavadocDeclaration"})
  private static TokenRequestBuilder aTokenRequest() {
    return TokenRequest.builder()
        .grantType("client_credentials")
        .clientId(TEST_CLIENT_ID)
        .clientSecret(TEST_CLIENT_SECRET)
        .audience(TEST_AUDIENCE)
        .subject(TEST_USER_1)
        .scope(TEST_SCOPE);
  }

  @Builder
  @JsonNaming(SnakeCaseStrategy.class)
  record TokenRequest(
      String grantType,
      String clientId,
      String clientSecret,
      String audience,
      String subject,
      String scope) {}

  @JsonNaming(SnakeCaseStrategy.class)
  record TokenResponse(String accessToken) {}
}
