package com.leminhbao.eventmanagement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UserDetailsResponse {

  private UUID id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String bio;
  private Boolean active;
  private Boolean emailVerified;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime lastLogin;
  private Set<RoleResponse> roles;

  @Data
  @Builder
  public static class RoleResponse {
    private UUID id;
    private String name;
    private String description;
  }
}
