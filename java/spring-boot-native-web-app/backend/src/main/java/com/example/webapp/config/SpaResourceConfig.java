package com.example.webapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Resolves static resources for the React single-page application (SPA).
 *
 * @see SpaResourceResolver
 */
@Configuration
@RequiredArgsConstructor
public class SpaResourceConfig implements WebMvcConfigurer {

  private final WebMvcProperties webMvcProperties;
  private final WebProperties webProperties;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler(webMvcProperties.getStaticPathPattern())
        .addResourceLocations(webProperties.getResources().getStaticLocations())
        .resourceChain(true)
        .addResolver(new SpaResourceResolver());
  }
}
