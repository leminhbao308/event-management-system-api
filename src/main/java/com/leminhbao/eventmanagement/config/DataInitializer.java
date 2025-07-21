package com.leminhbao.eventmanagement.config;

import com.leminhbao.eventmanagement.model.Role;
import com.leminhbao.eventmanagement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

  private final RoleRepository roleRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    initializeRoles();
  }

  private void initializeRoles() {
    log.info("Starting role initialization...");

    // Check if roles already exist
    if (roleRepository.count() > 0) {
      log.info("Roles already exist, skipping initialization");
      return;
    }

    // Create default roles
    createRoleIfNotExists("USER", "Default user role with basic permissions");
    createRoleIfNotExists("ORGANIZER", "Event organizer role with event management permissions");
    createRoleIfNotExists("ADMIN", "Administrator role with full system permissions");

    log.info("Role initialization completed successfully");
  }

  private void createRoleIfNotExists(String roleName, String description) {
    if (!roleRepository.existsByName(roleName)) {
      Role role = Role.builder()
          .name(roleName)
          .description(description)
          .build();

      roleRepository.save(role);
      log.info("Created role: {}", roleName);
    } else {
      log.debug("Role {} already exists", roleName);
    }
  }
}
