package com.leminhbao.eventmanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateEventRequest {

  @NotBlank(message = "Event name is required")
  @Size(max = 200, message = "Event name must not exceed 200 characters")
  private String name;

  @Size(max = 2000, message = "Description must not exceed 2000 characters")
  private String description;

  @NotNull(message = "Category ID is required")
  private UUID categoryId;

  @NotNull(message = "Start date and time is required")
  private LocalDateTime startDateTime;

  @NotNull(message = "End date and time is required")
  private LocalDateTime endDateTime;

  @NotBlank(message = "Location is required")
  @Size(max = 500, message = "Location must not exceed 500 characters")
  private String location;

  @Positive(message = "Max capacity must be positive")
  private Integer maxCapacity;

  @Positive(message = "Ticket price must be positive")
  private BigDecimal ticketPrice;

  private String imageUrl;

  private Boolean isActive = true;
}
