package com.leminhbao.eventmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class User extends BaseEntity {

  @Column(unique = true, nullable = false, length = 50)
  private String username;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "first_name", nullable = false, length = 50)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = 50)
  private String lastName;

  @Column(length = 20)
  private String phone;

  @Column(length = 500)
  private String bio;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "profile_image_url", length = 500)
  private String profileImageUrl;

  @Column(name = "is_active")
  @Builder.Default
  private Boolean isActive = true;

  @Column(name = "is_email_verified")
  @Builder.Default
  private Boolean isEmailVerified = false;

  @Column(name = "email_verification_token")
  private String emailVerificationToken;

  @Column(name = "password_reset_token")
  private String passwordResetToken;

  @Column(name = "password_reset_expires_at")
  private LocalDateTime passwordResetExpiresAt;

  @Column(name = "last_login_at")
  private LocalDateTime lastLoginAt;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  @Builder.Default
  private Set<Role> roles = new HashSet<>();

  public String getFullName() {
    return firstName + " " + lastName;
  }
}
