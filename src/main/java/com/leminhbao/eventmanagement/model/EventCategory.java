package com.leminhbao.eventmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "event_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class EventCategory extends BaseEntity {

  @Column(unique = true, nullable = false, length = 100)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "color_code", length = 7)
  private String colorCode;

  @Column(length = 50)
  private String icon;

  @Column(name = "is_active")
  @Builder.Default
  private Boolean isActive = true;
}
