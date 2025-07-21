package com.leminhbao.eventmanagement.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EventDetailsResponse {

  private UUID id;
  private String name;
  private String description;
  private CategoryResponse category;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private String location;
  private Integer maxCapacity;
  private Integer currentCapacity;
  private BigDecimal ticketPrice;
  private String imageUrl;
  private String status;
  private Boolean isActive;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private OrganizerResponse organizer;

  @Data
  @Builder
  public static class CategoryResponse {
    private UUID id;
    private String name;
    private String description;
  }

  @Data
  @Builder
  public static class OrganizerResponse {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
  }
}
