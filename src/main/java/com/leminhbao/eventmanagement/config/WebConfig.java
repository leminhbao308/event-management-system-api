package com.leminhbao.eventmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${app.cors.allowed-origins:http://localhost:3000,http://localhost:5173}")
  private String allowedOrigins;

  @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE,OPTIONS}")
  private String allowedMethods;

  @Value("${app.cors.allowed-headers:*}")
  private String allowedHeaders;

  @Value("${app.cors.allow-credentials:true}")
  private boolean allowCredentials;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    List<String> origins = Arrays.asList(allowedOrigins.split(","));
    List<String> methods = Arrays.asList(allowedMethods.split(","));
    List<String> headers = Arrays.asList(allowedHeaders.split(","));

    registry.addMapping("/api/v1/**")
        .allowedOrigins(origins.toArray(new String[0]))
        .allowedMethods(methods.toArray(new String[0]))
        .allowedHeaders(headers.toArray(new String[0]))
        .allowCredentials(allowCredentials)
        .maxAge(3600);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList(allowedOrigins.split(",")));
    configuration.setAllowedMethods(Arrays.asList(allowedMethods.split(",")));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(allowCredentials);
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/v1/**", configuration);
    return source;
  }
}
