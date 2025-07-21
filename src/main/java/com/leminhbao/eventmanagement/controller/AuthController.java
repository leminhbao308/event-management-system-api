package com.leminhbao.eventmanagement.controller;

import com.leminhbao.eventmanagement.dto.auth.JwtResponse;
import com.leminhbao.eventmanagement.dto.auth.LoginRequest;
import com.leminhbao.eventmanagement.dto.auth.RegisterRequest;
import com.leminhbao.eventmanagement.dto.common.ApiResponse;
import com.leminhbao.eventmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  @Operation(summary = "User login", description = "Authenticate user and return JWT token")
  public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
    JwtResponse jwtResponse = authService.login(loginRequest);
    return ResponseEntity.ok(ApiResponse.success("Login successful", jwtResponse));
  }

  @PostMapping("/register")
  @Operation(summary = "User registration", description = "Register a new user account")
  public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
    String message = authService.register(registerRequest);
    return ResponseEntity.ok(ApiResponse.success(message));
  }

  @PostMapping("/refresh")
  @Operation(summary = "Refresh token", description = "Refresh JWT token using refresh token")
  public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(@RequestParam String refreshToken) {
    JwtResponse jwtResponse = authService.refreshToken(refreshToken);
    return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", jwtResponse));
  }

  @PostMapping("/logout")
  @Operation(summary = "User logout", description = "Logout user (client should remove token)")
  public ResponseEntity<ApiResponse<String>> logout() {
    // In a stateless JWT implementation, logout is handled on the client side
    // by removing the token. For enhanced security, you could implement
    // a token blacklist here.
    return ResponseEntity.ok(ApiResponse.success("Logout successful"));
  }
}
