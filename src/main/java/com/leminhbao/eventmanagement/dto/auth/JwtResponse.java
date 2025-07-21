package com.leminhbao.eventmanagement.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

  private String token;
  private String refreshToken;
  @Builder.Default
  private String type = "Bearer";
  private String username;
  private String email;
  private Set<String> roles;
}
