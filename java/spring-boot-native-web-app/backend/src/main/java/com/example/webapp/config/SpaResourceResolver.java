package com.example.webapp.config;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.jspecify.annotations.Nullable;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * Resolves static resources for the React single-page application (SPA).
 *
 * <p>If a requested static resource does not exist, this resolver returns {@code index.html}. This
 * allows the React client-side router to handle direct requests to SPA routes.
 */
public class SpaResourceResolver extends PathResourceResolver {

  @Override
  protected @Nullable Resource resolveResourceInternal(
      @Nullable HttpServletRequest request,
      String requestPath,
      List<? extends Resource> locations,
      ResourceResolverChain chain) {
    var requestedResource = super.resolveResourceInternal(request, requestPath, locations, chain);
    if (requestedResource != null) {
      return requestedResource;
    }
    boolean acceptsHtml = Optional.ofNullable(request)
        .map(req -> req.getHeader(ACCEPT))
        .map(accept -> accept.contains(TEXT_HTML_VALUE))
        .orElse(false);
    if (acceptsHtml) {
      return super.resolveResourceInternal(request, "index.html", locations, chain);
    }
    return null;
  }
}
