package dev.sagar.alerta.alerting.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DiameterRange {
  @JsonProperty("estimated_diameter_min")
  private double estimatedDiameterMin;

  @JsonProperty("estimated_diameter_max")
  private double estimatedDiameterMax;
}
