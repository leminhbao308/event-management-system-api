package com.leminhbao.eventmanagement.repository;

import com.leminhbao.eventmanagement.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  Optional<User> findByUsernameOrEmail(String username, String email);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  Optional<User> findByEmailVerificationToken(String token);

  Optional<User> findByPasswordResetToken(String token);

  List<User> findByIsActiveTrue();

  Page<User> findByIsActive(Boolean active, Pageable pageable);

  @Query("SELECT u FROM User u WHERE " +
      "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
      "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
      "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
      "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))")
  Page<User> searchUsers(@Param("search") String search, Pageable pageable);

  @Query("SELECT u FROM User u WHERE u.lastLoginAt > :date")
  List<User> findActiveUsersSince(@Param("date") LocalDateTime date);

  @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
  Long countActiveUsers();

  @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
  List<User> findUsersByRole(@Param("roleName") String roleName);
}
