package com.leminhbao.eventmanagement.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

  private String secret = "mySecretKey123456789012345678901234567890";
  private long expiration = 86400000; // 24 hours in milliseconds
  private long refreshExpiration = 604800000; // 7 days in milliseconds
  private String header = "Authorization";
  private String prefix = "Bearer ";
}
