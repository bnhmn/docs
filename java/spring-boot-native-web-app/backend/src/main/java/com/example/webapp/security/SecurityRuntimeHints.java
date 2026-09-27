package com.example.webapp.security;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

public class SecurityRuntimeHints implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    // Workaround for GraalVM native image support with Redis-backed Spring Session. These hints are
    // required so Spring Security OAuth session data can be fully serialized and deserialized as
    // JSON at runtime, since the default native setup does not currently provide the required
    // reflection metadata. See https://github.com/spring-projects/spring-security/issues/18145
    TypeReference[] securityTypes = {
      // spotless:off
      // How the classes that need to be registered here were identified:
      // 1. The application was started locally in JVM mode, where everything works without issues.
      // 2. The web app was opened in the browser and a login was performed.
      // 3. The session data in the connected Redis instance was inspected using KEYS * and HGETALL.
      // 4. There, the @class names required for serialization were visible.
      // 5. These classes and each of their superclasses were then simply added to this config.
      TypeReference.of(com.nimbusds.oauth2.sdk.util.OrderedJSONObject.class),
      TypeReference.of(org.springframework.security.authentication.AbstractAuthenticationToken.class),
      TypeReference.of(org.springframework.security.core.authority.SimpleGrantedAuthority.class),
      TypeReference.of(org.springframework.security.core.context.SecurityContextImpl.class),
      TypeReference.of(org.springframework.security.oauth2.client.OAuth2AuthorizedClient.class),
      TypeReference.of(org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken.class),
      TypeReference.of(org.springframework.security.oauth2.client.registration.ClientRegistration.ClientSettings.class),
      TypeReference.of(org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails.UserInfoEndpoint.class),
      TypeReference.of(org.springframework.security.oauth2.client.registration.ClientRegistration.ProviderDetails.class),
      TypeReference.of(org.springframework.security.oauth2.client.registration.ClientRegistration.class),
      TypeReference.of(org.springframework.security.oauth2.core.AbstractOAuth2Token.class),
      TypeReference.of(org.springframework.security.oauth2.core.AuthenticationMethod.class),
      TypeReference.of(org.springframework.security.oauth2.core.AuthorizationGrantType.class),
      TypeReference.of(org.springframework.security.oauth2.core.ClientAuthenticationMethod.class),
      TypeReference.of(org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.class),
      TypeReference.of(org.springframework.security.oauth2.core.OAuth2AccessToken.class),
      TypeReference.of(org.springframework.security.oauth2.core.OAuth2RefreshToken.class),
      TypeReference.of(org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest.class),
      TypeReference.of(org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponseType.class),
      TypeReference.of(org.springframework.security.oauth2.core.oidc.OidcIdToken.class),
      TypeReference.of(org.springframework.security.oauth2.core.oidc.OidcUserInfo.class),
      TypeReference.of(org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser.class),
      TypeReference.of(org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority.class),
      TypeReference.of(org.springframework.security.oauth2.core.user.DefaultOAuth2User.class),
      TypeReference.of(org.springframework.security.oauth2.core.user.OAuth2UserAuthority.class),
      TypeReference.of(org.springframework.security.web.authentication.WebAuthenticationDetails.class),
      TypeReference.of(org.springframework.security.web.savedrequest.DefaultSavedRequest.class),
      TypeReference.of(org.springframework.security.web.savedrequest.SavedCookie.class),
      // In addition to the model classes, all relevant Jackson mixins provided by Spring Security
      // must be registered. If a mixin references other classes via annotations, such as a custom
      // deserializer, converter, or builder class, these must also be registered individually.
      TypeReference.of("org.springframework.security.jackson.SimpleGrantedAuthorityMixin"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.ClientRegistrationDeserializer"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.ClientRegistrationMixin"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.DefaultOidcUserMixin"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.OAuth2AccessTokenMixin"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.OAuth2AuthenticationTokenMixin"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.OAuth2AuthorizationRequestDeserializer"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.OAuth2AuthorizationRequestMixin"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.OAuth2AuthorizedClientMixin"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.OAuth2RefreshTokenMixin"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.OidcIdTokenMixin"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.OidcUserAuthorityMixin"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.OidcUserInfoMixin"),
      TypeReference.of("org.springframework.security.oauth2.client.jackson.StdConverters$AccessTokenTypeConverter"),
      TypeReference.of("org.springframework.security.web.jackson.DefaultSavedRequestMixin"),
      TypeReference.of("org.springframework.security.web.jackson.SavedCookieMixin"),
      TypeReference.of("org.springframework.security.web.jackson.WebAuthenticationDetailsMixin"),
      TypeReference.of("org.springframework.security.web.savedrequest.DefaultSavedRequest$Builder"),
      // spotless:on
    };
    for (var typeReference : securityTypes) {
      hints
          .reflection()
          .registerType(
              typeReference,
              builder -> builder.withMembers(
                  MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                  MemberCategory.INVOKE_DECLARED_METHODS,
                  MemberCategory.ACCESS_DECLARED_FIELDS));
    }
  }
}
