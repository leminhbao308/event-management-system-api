package com.leminhbao.eventmanagement.service;

import com.leminhbao.eventmanagement.dto.request.UpdateUserRequest;
import com.leminhbao.eventmanagement.dto.response.UserDetailsResponse;
import com.leminhbao.eventmanagement.model.Role;
import com.leminhbao.eventmanagement.model.User;
import com.leminhbao.eventmanagement.exception.ResourceNotFoundException;
import com.leminhbao.eventmanagement.repository.RoleRepository;
import com.leminhbao.eventmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminUserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Transactional(readOnly = true)
  public Page<UserDetailsResponse> getAllUsers(String search, Boolean active, Pageable pageable) {
    log.info("Fetching users with search: '{}', active: {}", search, active);

    Page<User> users;
    if (search != null && !search.trim().isEmpty()) {
      users = userRepository.searchUsers(search.trim(), pageable);
    } else if (active != null) {
      users = userRepository.findByIsActive(active, pageable);
    } else {
      users = userRepository.findAll(pageable);
    }

    return users.map(this::mapToUserDetailsResponse);
  }

  @Transactional(readOnly = true)
  public UserDetailsResponse getUserById(UUID userId) {
    log.info("Fetching user details for ID: {}", userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    return mapToUserDetailsResponse(user);
  }

  public UserDetailsResponse updateUser(UUID userId, UpdateUserRequest request) {
    log.info("Updating user with ID: {}", userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

    // Check if email is already taken by another user
    if (!user.getEmail().equals(request.getEmail()) &&
        userRepository.existsByEmail(request.getEmail()).booleanValue()) {
      throw new IllegalArgumentException("Email is already taken");
    }

    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setPhone(request.getPhoneNumber());
    user.setBio(request.getBio());
    user.setUpdatedAt(LocalDateTime.now());

    User savedUser = userRepository.save(user);
    log.info("User updated successfully: {}", savedUser.getUsername());
    return mapToUserDetailsResponse(savedUser);
  }

  public void activateUser(UUID userId) {
    log.info("Activating user with ID: {}", userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

    user.setIsActive(true);
    user.setUpdatedAt(LocalDateTime.now());
    userRepository.save(user);
    log.info("User activated successfully: {}", user.getUsername());
  }

  public void deactivateUser(UUID userId) {
    log.info("Deactivating user with ID: {}", userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

    user.setIsActive(false);
    user.setUpdatedAt(LocalDateTime.now());
    userRepository.save(user);
    log.info("User deactivated successfully: {}", user.getUsername());
  }

  public void assignRole(UUID userId, UUID roleId) {
    log.info("Assigning role {} to user {}", roleId, userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

    Role role = roleRepository.findById(roleId)
        .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

    user.getRoles().add(role);
    user.setUpdatedAt(LocalDateTime.now());
    userRepository.save(user);
    log.info("Role {} assigned to user {} successfully", role.getName(), user.getUsername());
  }

  public void removeRole(UUID userId, UUID roleId) {
    log.info("Removing role {} from user {}", roleId, userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

    Role role = roleRepository.findById(roleId)
        .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + roleId));

    user.getRoles().remove(role);
    user.setUpdatedAt(LocalDateTime.now());
    userRepository.save(user);
    log.info("Role {} removed from user {} successfully", role.getName(), user.getUsername());
  }

  public void deleteUser(UUID userId) {
    log.info("Deleting user with ID: {}", userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

    userRepository.delete(user);
    log.info("User deleted successfully: {}", user.getUsername());
  }

  private UserDetailsResponse mapToUserDetailsResponse(User user) {
    Set<UserDetailsResponse.RoleResponse> roleResponses = user.getRoles().stream()
        .map(role -> UserDetailsResponse.RoleResponse.builder()
            .id(role.getId())
            .name(role.getName())
            .description(role.getDescription())
            .build())
        .collect(Collectors.toSet());

    return UserDetailsResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .phoneNumber(user.getPhone())
        .bio(user.getBio())
        .active(user.getIsActive())
        .emailVerified(user.getIsEmailVerified())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .lastLogin(user.getLastLoginAt())
        .roles(roleResponses)
        .build();
  }
}
