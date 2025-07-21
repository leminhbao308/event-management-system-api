package com.leminhbao.eventmanagement.repository;

import com.leminhbao.eventmanagement.model.Event;
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
public interface EventRepository extends JpaRepository<Event, UUID> {

  Optional<Event> findBySlug(String slug);

  Page<Event> findByIsPublishedTrueAndIsPublicTrue(Pageable pageable);

  Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);

  List<Event> findByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);

  @Query("SELECT e FROM Event e WHERE e.isPublished = true AND e.isPublic = true " +
      "AND e.startDateTime >= :startDate ORDER BY e.startDateTime ASC")
  List<Event> findUpcomingPublicEvents(@Param("startDate") LocalDateTime startDate);

  @Query("SELECT e FROM Event e WHERE e.title LIKE %:keyword% OR e.description LIKE %:keyword%")
  Page<Event> searchEvents(@Param("keyword") String keyword, Pageable pageable);

  @Query("SELECT COUNT(e) FROM Event e WHERE e.isPublished = true")
  Long countPublishedEvents();

  @Query("SELECT COUNT(e) FROM Event e WHERE e.organizer.id = :organizerId")
  Long countEventsByOrganizer(@Param("organizerId") UUID organizerId);

  List<Event> findByCategoryId(UUID categoryId);
}
