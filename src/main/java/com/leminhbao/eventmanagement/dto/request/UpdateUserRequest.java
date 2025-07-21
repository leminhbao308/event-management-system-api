package com.leminhbao.eventmanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

  @NotBlank(message = "First name is required")
  @Size(max = 100, message = "First name must not exceed 100 characters")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Size(max = 100, message = "Last name must not exceed 100 characters")
  private String lastName;

  @Email(message = "Please provide a valid email address")
  @NotBlank(message = "Email is required")
  @Size(max = 255, message = "Email must not exceed 255 characters")
  private String email;

  @Size(max = 20, message = "Phone number must not exceed 20 characters")
  private String phoneNumber;

  @Size(max = 500, message = "Bio must not exceed 500 characters")
  private String bio;
}
