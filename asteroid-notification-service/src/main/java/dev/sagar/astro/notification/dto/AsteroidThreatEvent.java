package dev.sagar.astro.notification.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsteroidThreatEvent {
  private String asteroidId;
  private String name;
  private double estimatedDiameterMaxKm;
  private boolean isPotentiallyHazardous;
  private LocalDate closeApproachDate;
  private double missDistanceKm;
  private double relativeVelocityKmph;
  private String nasaJplUrl;
}
