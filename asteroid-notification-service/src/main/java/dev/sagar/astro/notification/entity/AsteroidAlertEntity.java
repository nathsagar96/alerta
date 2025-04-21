package dev.sagar.astro.notification.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "asteroid_alerts")
public class AsteroidAlertEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "asteroid_id", nullable = false, unique = true)
  private String asteroidId;

  @Column(nullable = false)
  private String name;

  @Column(name = "estimated_diameter_max_km", nullable = false)
  private double estimatedDiameterMaxKm;

  @Column(name = "is_potentially_hazardous", nullable = false)
  private boolean isPotentiallyHazardous;

  @Column(name = "close_approach_date", nullable = false)
  private LocalDate closeApproachDate;

  @Column(name = "miss_distance_km", nullable = false)
  private double missDistanceKm;

  @Column(name = "relative_velocity_kmph", nullable = false)
  private double relativeVelocityKmph;

  @Column(name = "nasa_jpl_url")
  private String nasaJplUrl;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "notification_sent", nullable = false)
  private boolean notificationSent;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }
}
