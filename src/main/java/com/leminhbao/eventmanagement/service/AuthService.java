package com.leminhbao.eventmanagement.service;

import com.leminhbao.eventmanagement.dto.auth.JwtResponse;
import com.leminhbao.eventmanagement.dto.auth.LoginRequest;
import com.leminhbao.eventmanagement.dto.auth.RegisterRequest;
import com.leminhbao.eventmanagement.exception.BadRequestException;
import com.leminhbao.eventmanagement.exception.ResourceNotFoundException;
import com.leminhbao.eventmanagement.model.Role;
import com.leminhbao.eventmanagement.model.User;
import com.leminhbao.eventmanagement.repository.RoleRepository;
import com.leminhbao.eventmanagement.repository.UserRepository;
import com.leminhbao.eventmanagement.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider tokenProvider;

  @Transactional
  public JwtResponse login(LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(),
            loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String jwt = tokenProvider.generateToken(authentication);
    String refreshToken = tokenProvider.generateRefreshToken(authentication.getName());

    User user = userRepository.findByUsernameOrEmail(loginRequest.getUsername(), loginRequest.getUsername())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    // Update last login time
    user.setLastLoginAt(LocalDateTime.now());
    userRepository.save(user);

    Set<String> roles = user.getRoles().stream()
        .map(Role::getName)
        .collect(Collectors.toSet());

    return JwtResponse.builder()
        .token(jwt)
        .refreshToken(refreshToken)
        .username(user.getUsername())
        .email(user.getEmail())
        .roles(roles)
        .build();
  }

  @Transactional
  public String register(RegisterRequest registerRequest) {
    if (userRepository.existsByUsername(registerRequest.getUsername())) {
      throw new BadRequestException("Username is already taken!");
    }

    if (userRepository.existsByEmail(registerRequest.getEmail())) {
      throw new BadRequestException("Email is already in use!");
    }

    // Get default USER role
    Role userRole = roleRepository.findByName("USER")
        .orElseThrow(() -> new ResourceNotFoundException("User Role not found"));

    Set<Role> roles = new HashSet<>();
    roles.add(userRole);

    User user = User.builder()
        .username(registerRequest.getUsername())
        .email(registerRequest.getEmail())
        .passwordHash(passwordEncoder.encode(registerRequest.getPassword()))
        .firstName(registerRequest.getFirstName())
        .lastName(registerRequest.getLastName())
        .phone(registerRequest.getPhone())
        .roles(roles)
        .isActive(true)
        .isEmailVerified(false)
        .build();

    User savedUser = userRepository.save(user);
    log.info("User registered successfully: {}", savedUser.getUsername());

    return "User registered successfully";
  }

  public JwtResponse refreshToken(String refreshToken) {
    if (tokenProvider.validateToken(refreshToken)) {
      String username = tokenProvider.getUsernameFromToken(refreshToken);
      User user = userRepository.findByUsername(username)
          .orElseThrow(() -> new ResourceNotFoundException("User not found"));

      String newToken = tokenProvider.generateToken(username);
      String newRefreshToken = tokenProvider.generateRefreshToken(username);

      Set<String> roles = user.getRoles().stream()
          .map(Role::getName)
          .collect(Collectors.toSet());

      return JwtResponse.builder()
          .token(newToken)
          .refreshToken(newRefreshToken)
          .username(user.getUsername())
          .email(user.getEmail())
          .roles(roles)
          .build();
    } else {
      throw new BadRequestException("Invalid refresh token");
    }
  }
}
