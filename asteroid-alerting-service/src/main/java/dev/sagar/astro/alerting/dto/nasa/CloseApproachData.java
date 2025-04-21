package dev.sagar.astro.alerting.dto.nasa;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CloseApproachData {
  @JsonProperty("close_approach_date")
  private String closeApproachDate;

  @JsonProperty("miss_distance")
  private MissDistance missDistance;

  @JsonProperty("relative_velocity")
  private RelativeVelocity relativeVelocity;
}
