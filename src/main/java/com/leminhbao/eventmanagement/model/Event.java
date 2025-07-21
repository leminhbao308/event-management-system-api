package com.leminhbao.eventmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class Event extends BaseEntity {

  @Column(nullable = false, length = 200)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "short_description", length = 500)
  private String shortDescription;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private EventCategory category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organizer_id", nullable = false)
  private User organizer;

  @Column(name = "venue_name", length = 200)
  private String venueName;

  @Column(name = "venue_address", columnDefinition = "TEXT")
  private String venueAddress;

  @Column(name = "venue_city", length = 100)
  private String venueCity;

  @Column(name = "venue_state", length = 100)
  private String venueState;

  @Column(name = "venue_country", length = 100)
  private String venueCountry;

  @Column(name = "venue_postal_code", length = 20)
  private String venuePostalCode;

  @Column(name = "venue_latitude", precision = 10, scale = 8)
  private BigDecimal venueLatitude;

  @Column(name = "venue_longitude", precision = 11, scale = 8)
  private BigDecimal venueLongitude;

  @Column(name = "start_date_time", nullable = false)
  private LocalDateTime startDateTime;

  @Column(name = "end_date_time", nullable = false)
  private LocalDateTime endDateTime;

  @Column(length = 50)
  @Builder.Default
  private String timezone = "UTC";

  @Column(name = "max_attendees")
  private Integer maxAttendees;

  @Column(name = "current_attendees")
  @Builder.Default
  private Integer currentAttendees = 0;

  @Column(name = "registration_start_date")
  private LocalDateTime registrationStartDate;

  @Column(name = "registration_end_date")
  private LocalDateTime registrationEndDate;

  @Column(name = "ticket_price", precision = 10, scale = 2)
  @Builder.Default
  private BigDecimal ticketPrice = BigDecimal.ZERO;

  @Column(length = 3)
  @Builder.Default
  private String currency = "USD";

  @Column(name = "is_free")
  @Builder.Default
  private Boolean isFree = true;

  @Column(name = "is_public")
  @Builder.Default
  private Boolean isPublic = true;

  @Column(name = "is_published")
  @Builder.Default
  private Boolean isPublished = false;

  @Column(name = "is_cancelled")
  @Builder.Default
  private Boolean isCancelled = false;

  @Column(name = "cancellation_reason", columnDefinition = "TEXT")
  private String cancellationReason;

  @Column(name = "event_image_url", length = 500)
  private String eventImageUrl;

  @Column(name = "event_banner_url", length = 500)
  private String eventBannerUrl;

  @ElementCollection
  @CollectionTable(name = "event_tags", joinColumns = @JoinColumn(name = "event_id"))
  @Column(name = "tag")
  private List<String> tags;

  @Column(columnDefinition = "TEXT")
  private String requirements;

  @Column(name = "what_to_bring", columnDefinition = "TEXT")
  private String whatToBring;

  @Column(name = "refund_policy", columnDefinition = "TEXT")
  private String refundPolicy;

  @Column(name = "contact_email", length = 100)
  private String contactEmail;

  @Column(name = "contact_phone", length = 20)
  private String contactPhone;

  @Column(name = "external_url", length = 500)
  private String externalUrl;

  @Column(unique = true)
  private String slug;

  @Column(name = "meta_title", length = 200)
  private String metaTitle;

  @Column(name = "meta_description", length = 500)
  private String metaDescription;
}
