package com.leminhbao.eventmanagement.controller.admin;

import com.leminhbao.eventmanagement.dto.common.ApiResponse;
import com.leminhbao.eventmanagement.dto.request.UpdateUserRequest;
import com.leminhbao.eventmanagement.dto.response.UserDetailsResponse;
import com.leminhbao.eventmanagement.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@Tag(name = "Admin User Management", description = "Admin APIs for user management")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

  private final AdminUserService adminUserService;

  @GetMapping
  @Operation(summary = "Get all users with pagination")
  public ResponseEntity<ApiResponse<Page<UserDetailsResponse>>> getAllUsers(
      @RequestParam(defaultValue = "") String search,
      @RequestParam(defaultValue = "true") Boolean active,
      Pageable pageable) {
    Page<UserDetailsResponse> users = adminUserService.getAllUsers(search, active, pageable);
    return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
  }

  @GetMapping("/{userId}")
  @Operation(summary = "Get user details by ID")
  public ResponseEntity<ApiResponse<UserDetailsResponse>> getUserById(@PathVariable UUID userId) {
    UserDetailsResponse user = adminUserService.getUserById(userId);
    return ResponseEntity.ok(ApiResponse.success("User details retrieved successfully", user));
  }

  @PutMapping("/{userId}")
  @Operation(summary = "Update user details")
  public ResponseEntity<ApiResponse<UserDetailsResponse>> updateUser(
      @PathVariable UUID userId,
      @Valid @RequestBody UpdateUserRequest request) {
    UserDetailsResponse updatedUser = adminUserService.updateUser(userId, request);
    return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
  }

  @PutMapping("/{userId}/activate")
  @Operation(summary = "Activate user account")
  public ResponseEntity<ApiResponse<String>> activateUser(@PathVariable UUID userId) {
    adminUserService.activateUser(userId);
    return ResponseEntity.ok(ApiResponse.success("User activated successfully", null));
  }

  @PutMapping("/{userId}/deactivate")
  @Operation(summary = "Deactivate user account")
  public ResponseEntity<ApiResponse<String>> deactivateUser(@PathVariable UUID userId) {
    adminUserService.deactivateUser(userId);
    return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", null));
  }

  @PutMapping("/{userId}/roles/{roleId}")
  @Operation(summary = "Assign role to user")
  public ResponseEntity<ApiResponse<String>> assignRole(
      @PathVariable UUID userId,
      @PathVariable UUID roleId) {
    adminUserService.assignRole(userId, roleId);
    return ResponseEntity.ok(ApiResponse.success("Role assigned successfully", null));
  }

  @DeleteMapping("/{userId}/roles/{roleId}")
  @Operation(summary = "Remove role from user")
  public ResponseEntity<ApiResponse<String>> removeRole(
      @PathVariable UUID userId,
      @PathVariable UUID roleId) {
    adminUserService.removeRole(userId, roleId);
    return ResponseEntity.ok(ApiResponse.success("Role removed successfully", null));
  }

  @DeleteMapping("/{userId}")
  @Operation(summary = "Delete user account")
  public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable UUID userId) {
    adminUserService.deleteUser(userId);
    return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
  }
}
