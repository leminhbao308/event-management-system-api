package com.leminhbao.eventmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class Role extends BaseEntity {

  @Column(unique = true, nullable = false, length = 50)
  private String name;

  @Column(length = 255)
  private String description;

  @ManyToMany(mappedBy = "roles")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @Builder.Default
  private Set<User> users = new HashSet<>();
}
