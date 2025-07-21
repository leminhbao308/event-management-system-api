package com.leminhbao.eventmanagement.service;

import com.leminhbao.eventmanagement.model.AuditLog;
import com.leminhbao.eventmanagement.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuditService {

  private final AuditLogRepository auditLogRepository;

  public void logActivity(String action, String entityType, UUID entityId, String details) {
    logActivity(action, entityType, entityId, details, getCurrentUsername());
  }

  public void logActivity(String action, String entityType, UUID entityId, String details, String username) {
    try {
      AuditLog auditLog = AuditLog.builder()
          .action(action)
          .entityType(entityType)
          .entityId(entityId)
          .details(details)
          .username(username)
          .timestamp(LocalDateTime.now())
          .ipAddress(getClientIpAddress())
          .userAgent(getUserAgent())
          .build();

      auditLogRepository.save(auditLog);
      log.debug("Audit log created: {} {} by {}", action, entityType, username);
    } catch (Exception e) {
      log.error("Failed to create audit log: {}", e.getMessage(), e);
    }
  }

  public void logUserAction(String action, UUID userId, String details) {
    logActivity(action, "USER", userId, details);
  }

  public void logEventAction(String action, UUID eventId, String details) {
    logActivity(action, "EVENT", eventId, details);
  }

  public void logSystemAction(String action, String details) {
    logActivity(action, "SYSTEM", null, details, "SYSTEM");
  }

  public void logSecurityEvent(String action, String details) {
    logActivity("SECURITY_" + action, "SECURITY", null, details);
  }

  private String getCurrentUsername() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication != null && authentication.isAuthenticated() &&
          !authentication.getName().equals("anonymousUser")) {
        return authentication.getName();
      }
    } catch (Exception e) {
      log.warn("Failed to get current username: {}", e.getMessage());
    }
    return "ANONYMOUS";
  }

  private String getClientIpAddress() {
    // This would be implemented with HttpServletRequest in a real application
    // For now, returning null as we don't have access to the request here
    return null;
  }

  private String getUserAgent() {
    // This would be implemented with HttpServletRequest in a real application
    // For now, returning null as we don't have access to the request here
    return null;
  }
}
