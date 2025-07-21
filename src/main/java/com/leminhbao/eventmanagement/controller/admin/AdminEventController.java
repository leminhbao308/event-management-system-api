package com.leminhbao.eventmanagement.controller.admin;

import com.leminhbao.eventmanagement.dto.common.ApiResponse;
import com.leminhbao.eventmanagement.dto.request.CreateEventRequest;
import com.leminhbao.eventmanagement.dto.request.UpdateEventRequest;
import com.leminhbao.eventmanagement.dto.response.EventDetailsResponse;
import com.leminhbao.eventmanagement.service.AdminEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/events")
@Tag(name = "Admin Event Management", description = "Admin APIs for event management")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminEventController {

  private final AdminEventService adminEventService;

  @GetMapping
  @Operation(summary = "Get all events with pagination and filtering")
  public ResponseEntity<ApiResponse<Page<EventDetailsResponse>>> getAllEvents(
      @RequestParam(defaultValue = "") String search,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) UUID categoryId,
      Pageable pageable) {
    Page<EventDetailsResponse> events = adminEventService.getAllEvents(search, status, categoryId, pageable);
    return ResponseEntity.ok(ApiResponse.success("Events retrieved successfully", events));
  }

  @GetMapping("/{eventId}")
  @Operation(summary = "Get event details by ID")
  public ResponseEntity<ApiResponse<EventDetailsResponse>> getEventById(@PathVariable UUID eventId) {
    EventDetailsResponse event = adminEventService.getEventById(eventId);
    return ResponseEntity.ok(ApiResponse.success("Event details retrieved successfully", event));
  }

  @PostMapping
  @Operation(summary = "Create new event")
  public ResponseEntity<ApiResponse<EventDetailsResponse>> createEvent(@Valid @RequestBody CreateEventRequest request) {
    EventDetailsResponse createdEvent = adminEventService.createEvent(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.success("Event created successfully", createdEvent));
  }

  @PutMapping("/{eventId}")
  @Operation(summary = "Update event details")
  public ResponseEntity<ApiResponse<EventDetailsResponse>> updateEvent(
      @PathVariable UUID eventId,
      @Valid @RequestBody UpdateEventRequest request) {
    EventDetailsResponse updatedEvent = adminEventService.updateEvent(eventId, request);
    return ResponseEntity.ok(ApiResponse.success("Event updated successfully", updatedEvent));
  }

  @PutMapping("/{eventId}/publish")
  @Operation(summary = "Publish event")
  public ResponseEntity<ApiResponse<String>> publishEvent(@PathVariable UUID eventId) {
    adminEventService.publishEvent(eventId);
    return ResponseEntity.ok(ApiResponse.success("Event published successfully", null));
  }

  @PutMapping("/{eventId}/unpublish")
  @Operation(summary = "Unpublish event")
  public ResponseEntity<ApiResponse<String>> unpublishEvent(@PathVariable UUID eventId) {
    adminEventService.unpublishEvent(eventId);
    return ResponseEntity.ok(ApiResponse.success("Event unpublished successfully", null));
  }

  @PutMapping("/{eventId}/cancel")
  @Operation(summary = "Cancel event")
  public ResponseEntity<ApiResponse<String>> cancelEvent(@PathVariable UUID eventId) {
    adminEventService.cancelEvent(eventId);
    return ResponseEntity.ok(ApiResponse.success("Event cancelled successfully", null));
  }

  @DeleteMapping("/{eventId}")
  @Operation(summary = "Delete event")
  public ResponseEntity<ApiResponse<String>> deleteEvent(@PathVariable UUID eventId) {
    adminEventService.deleteEvent(eventId);
    return ResponseEntity.ok(ApiResponse.success("Event deleted successfully", null));
  }

  @GetMapping("/statistics")
  @Operation(summary = "Get event statistics")
  public ResponseEntity<ApiResponse<Object>> getEventStatistics() {
    Object statistics = adminEventService.getEventStatistics();
    return ResponseEntity.ok(ApiResponse.success("Event statistics retrieved successfully", statistics));
  }
}
