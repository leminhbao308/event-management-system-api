package com.leminhbao.eventmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

  @Value("${app.cors.allowed-origins}")
  private String allowedOrigins;

  @Value("${app.cors.allowed-methods}")
  private String allowedMethods;

  @Value("${app.cors.allowed-headers}")
  private String allowedHeaders;

  @Value("${app.cors.allow-credentials}")
  private boolean allowCredentials;

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // Set allowed origins
    List<String> origins = Arrays.asList(allowedOrigins.split(","));
    configuration.setAllowedOriginPatterns(origins);

    // Set allowed methods
    List<String> methods = Arrays.asList(allowedMethods.split(","));
    configuration.setAllowedMethods(methods);

    // Set allowed headers
    if ("*".equals(allowedHeaders)) {
      configuration.addAllowedHeader("*");
    } else {
      List<String> headers = Arrays.asList(allowedHeaders.split(","));
      configuration.setAllowedHeaders(headers);
    }

    // Set credentials
    configuration.setAllowCredentials(allowCredentials);

    // Expose headers that client can access
    configuration.setExposedHeaders(Arrays.asList(
        "Authorization",
        "Content-Type",
        "X-Requested-With",
        "accept",
        "Origin",
        "Access-Control-Request-Method",
        "Access-Control-Request-Headers"));

    // Apply configuration to all paths
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
