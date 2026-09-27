package com.example.webapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.jackson.CoreJacksonModule;
import org.springframework.security.oauth2.client.jackson.OAuth2ClientJacksonModule;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.jackson.WebJacksonModule;
import org.springframework.security.web.jackson.WebServletJacksonModule;
import org.springframework.security.web.server.jackson.WebServerJacksonModule;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@ImportRuntimeHints(SecurityRuntimeHints.class)
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(
      HttpSecurity http, LogoutSuccessHandler oidcClientInitiatedLogoutHandler) {
    // spotless:off
    // Secures all endpoints except /actuator/health and "/logout/oauth2/idp": users must sign in
    // via OIDC and stay signed in until the session expires (after 14 days of inactivity).
    return http
        // Set up CSRF protection for JavaScript SPA
        // https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa
        .csrf(CsrfConfigurer::spa)
        // Enable OIDC login
        // https://docs.spring.io/spring-security/reference/servlet/oauth2/login/index.html
        .oauth2Login(withDefaults())
        .oauth2Client(it -> it
            // Store access and refresh tokens in the Redis-backed session. In-memory sessions
            // would not be stable across instances and would be lost on every restart.
            .authorizedClientRepository(new HttpSessionOAuth2AuthorizedClientRepository()))
        .logout(it -> it
            // "OIDC RP-Initiated Logout": redirects to the IDP after the local logout so that the
            // user's session there ends as well
            .logoutSuccessHandler(oidcClientInitiatedLogoutHandler))
        .oidcLogout(withDefaults())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/health/**").permitAll()
            .requestMatchers("/logout/oauth2/idp").permitAll() // "OIDC Front-Channel Logout"
            .anyRequest().authenticated()
        )
        .build();
    // spotless:on
  }

  @Bean
  LogoutSuccessHandler oidcClientInitiatedLogoutHandler(
      ClientRegistrationRepository clientRegistrationRepository) {
    var logoutHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
    // Sets the location that the End-User's User Agent will be redirected to
    // after the logout has been performed at the Provider
    // https://docs.spring.io/spring-security/reference/servlet/oauth2/login/logout.html#configure-client-initiated-oidc-logout
    logoutHandler.setPostLogoutRedirectUri("{baseUrl}/login");
    return logoutHandler;
  }

  @Bean
  CookieSerializer cookieSerializer() {
    // Configure session cookie with secure defaults:
    // * HttpOnly to prevent JavaScript from accessing the cookie (mitigates XSS impact)
    // * Secure to ensure the cookie is only sent over HTTPS (protects against sniffing)
    // * SameSite=Lax to reduce CSRF risk while still allowing normal navigation-based flows
    // https://www.rfc-editor.org/rfc/rfc10017.html#name-cookie-security
    var cookieSerializer = new DefaultCookieSerializer();
    cookieSerializer.setUseHttpOnlyCookie(true);
    cookieSerializer.setUseSecureCookie(true);
    cookieSerializer.setSameSite("Lax");
    return cookieSerializer;
  }

  @Bean
  RedisSerializer<Object> springSessionDefaultRedisSerializer() {
    // Configures Spring Session to use a JSON-based Redis serializer instead of Java native
    // serialization. This is a workaround for GraalVM Native Image serialization issues with
    // Spring Security classes.
    return GenericJacksonJsonRedisSerializer.builder(() -> JsonMapper.builder()
            .addModule(new CoreJacksonModule())
            .addModule(new WebJacksonModule())
            .addModule(new WebServerJacksonModule())
            .addModule(new WebServletJacksonModule())
            .addModule(new OAuth2ClientJacksonModule()))
        .enableUnsafeDefaultTyping()
        .build();
  }
}
