package dev.sagar.astro.alerting.task;

import dev.sagar.astro.alerting.dto.AsteroidThreatEvent;
import dev.sagar.astro.alerting.dto.nasa.CloseApproachData;
import dev.sagar.astro.alerting.dto.nasa.NearEarthObject;
import dev.sagar.astro.alerting.service.NasaNeoService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NasaNeoScheduledTask {

  private final NasaNeoService nasaNeoService;
  private final KafkaTemplate<String, AsteroidThreatEvent> kafkaTemplate;
  private final String kafkaTopic;
  private final int daysAhead;
  private final double missDistanceThresholdKm;
  private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public NasaNeoScheduledTask(
      NasaNeoService nasaNeoService,
      KafkaTemplate<String, AsteroidThreatEvent> kafkaTemplate,
      @Value("${kafka.topic.asteroid-threats}") String kafkaTopic,
      @Value("${neo.check.days_ahead}") int daysAhead,
      @Value("${notification.miss_distance.threshold.km}") double missDistanceThresholdKm) {
    this.nasaNeoService = nasaNeoService;
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaTopic = kafkaTopic;
    this.daysAhead = daysAhead;
    this.missDistanceThresholdKm = missDistanceThresholdKm;
  }

  @Scheduled(cron = "${scheduler.cron.expression}")
  public void checkForAsteroidThreats() {
    LocalDate today = LocalDate.now();
    LocalDate endDate = today.plusDays(daysAhead);

    log.info("Scheduled task started: Checking for asteroid threats from {} to {}", today, endDate);

    List<NearEarthObject> neoList = nasaNeoService.fetchNeoData(today, endDate);
    log.info("Found {} NEOs in the time period", neoList.size());

    neoList.stream().filter(this::isPotentialThreat).forEach(this::publishThreatEvent);
  }

  private boolean isPotentialThreat(NearEarthObject neo) {
    if (!neo.isPotentiallyHazardous()) {
      return false;
    }

    if (!neo.getCloseApproachData().isEmpty()) {
      CloseApproachData closestApproach = neo.getCloseApproachData().get(0);
      double missDistanceKm = Double.parseDouble(closestApproach.getMissDistance().getKilometers());

      return missDistanceKm <= missDistanceThresholdKm;
    }

    return false;
  }

  private void publishThreatEvent(NearEarthObject neo) {
    try {
      CloseApproachData approach = neo.getCloseApproachData().get(0);

      AsteroidThreatEvent threatEvent =
          AsteroidThreatEvent.builder()
              .asteroidId(neo.getId())
              .name(neo.getName())
              .estimatedDiameterMaxKm(
                  neo.getEstimatedDiameter().getKilometers().getEstimatedDiameterMax())
              .isPotentiallyHazardous(neo.isPotentiallyHazardous())
              .closeApproachDate(LocalDate.parse(approach.getCloseApproachDate(), dateFormatter))
              .missDistanceKm(Double.parseDouble(approach.getMissDistance().getKilometers()))
              .relativeVelocityKmph(
                  Double.parseDouble(approach.getRelativeVelocity().getKilometersPerHour()))
              .nasaJplUrl(neo.getNasaJplUrl())
              .build();

      log.info(
          "Publishing threat event for asteroid: {} (ID: {})",
          threatEvent.getName(),
          threatEvent.getAsteroidId());

      kafkaTemplate
          .send(kafkaTopic, threatEvent.getAsteroidId(), threatEvent)
          .thenAccept(
              result ->
                  log.debug(
                      "Message sent to topic {} partition {} offset {}",
                      result.getRecordMetadata().topic(),
                      result.getRecordMetadata().partition(),
                      result.getRecordMetadata().offset()))
          .exceptionally(
              ex -> {
                log.error("Failed to send message to Kafka", ex);
                return null;
              });
    } catch (Exception e) {
      log.error("Error while creating or publishing threat event for NEO ID: {}", neo.getId(), e);
    }
  }
}
