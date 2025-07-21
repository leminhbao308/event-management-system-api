package com.leminhbao.eventmanagement.service;

import com.leminhbao.eventmanagement.model.User;
import com.leminhbao.eventmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));

    return UserPrincipal.create(user);
  }

  @Transactional
  public UserDetails loadUserById(String id) {
    User user = userRepository.findById(java.util.UUID.fromString(id))
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));

    return UserPrincipal.create(user);
  }

  public static class UserPrincipal implements UserDetails {
    private String id;
    private String username;
    private String email;
    private String password;
    private List<GrantedAuthority> authorities;
    private boolean enabled;

    public UserPrincipal(String id, String username, String email, String password,
        List<GrantedAuthority> authorities, boolean enabled) {
      this.id = id;
      this.username = username;
      this.email = email;
      this.password = password;
      this.authorities = authorities;
      this.enabled = enabled;
    }

    public static UserPrincipal create(User user) {
      List<GrantedAuthority> authorities = user.getRoles().stream()
          .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
          .collect(Collectors.toList());

      return new UserPrincipal(
          user.getId().toString(),
          user.getUsername(),
          user.getEmail(),
          user.getPasswordHash(),
          authorities,
          user.getIsActive());
    }

    public String getId() {
      return id;
    }

    public String getEmail() {
      return email;
    }

    @Override
    public String getUsername() {
      return username;
    }

    @Override
    public String getPassword() {
      return password;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
      return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
      return true;
    }

    @Override
    public boolean isAccountNonLocked() {
      return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }

    @Override
    public boolean isEnabled() {
      return enabled;
    }
  }
}
