package com.example.demo.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.oauth2.core.authorization.OAuth2AuthorizationManagers.hasScope;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) {
    // spotless:off
    // All API endpoints except /actuator/health are secured with JWT token auth to verify the
    // user's identity and permissions for the respective endpoint.
    return http
        .csrf(AbstractHttpConfigurer::disable) // Stateless REST APIs don't require CSRF protection.
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        // https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/health/**").permitAll()
            .requestMatchers("/me/playlist/**").access(hasScope("playlist"))
            .anyRequest().authenticated())
        .build();
    // spotless:on
  }
}
