package com.leminhbao.eventmanagement.repository;

import com.leminhbao.eventmanagement.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

  Page<AuditLog> findByUsernameOrderByTimestampDesc(String username, Pageable pageable);

  Page<AuditLog> findByEntityTypeOrderByTimestampDesc(String entityType, Pageable pageable);

  Page<AuditLog> findByEntityIdOrderByTimestampDesc(UUID entityId, Pageable pageable);

  Page<AuditLog> findByActionOrderByTimestampDesc(String action, Pageable pageable);

  @Query("SELECT a FROM AuditLog a WHERE a.timestamp BETWEEN :startDate AND :endDate ORDER BY a.timestamp DESC")
  Page<AuditLog> findByTimestampBetween(@Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      Pageable pageable);

  @Query("SELECT a FROM AuditLog a WHERE " +
      "(:username IS NULL OR LOWER(a.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
      "(:entityType IS NULL OR a.entityType = :entityType) AND " +
      "(:action IS NULL OR LOWER(a.action) LIKE LOWER(CONCAT('%', :action, '%'))) AND " +
      "(:startDate IS NULL OR a.timestamp >= :startDate) AND " +
      "(:endDate IS NULL OR a.timestamp <= :endDate) " +
      "ORDER BY a.timestamp DESC")
  Page<AuditLog> findWithFilters(@Param("username") String username,
      @Param("entityType") String entityType,
      @Param("action") String action,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      Pageable pageable);

  @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.timestamp >= :date")
  Long countActivitiesSince(@Param("date") LocalDateTime date);

  @Query("SELECT a.action, COUNT(a) FROM AuditLog a " +
      "WHERE a.timestamp >= :startDate " +
      "GROUP BY a.action ORDER BY COUNT(a) DESC")
  List<Object[]> getTopActionsByCount(@Param("startDate") LocalDateTime startDate, Pageable pageable);

  @Query("SELECT a.username, COUNT(a) FROM AuditLog a " +
      "WHERE a.timestamp >= :startDate AND a.username != 'SYSTEM' " +
      "GROUP BY a.username ORDER BY COUNT(a) DESC")
  List<Object[]> getTopUsersByActivity(@Param("startDate") LocalDateTime startDate, Pageable pageable);
}
