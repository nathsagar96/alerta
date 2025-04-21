package dev.sagar.astro.notification.service;

import dev.sagar.astro.notification.dto.AsteroidThreatEvent;
import dev.sagar.astro.notification.entity.AsteroidAlertEntity;
import dev.sagar.astro.notification.repository.AsteroidAlertRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationHandlerService {

  private final AsteroidAlertRepository asteroidAlertRepository;
  private final EmailService emailService;

  @Transactional
  public void processAsteroidThreat(AsteroidThreatEvent threatEvent) {
    log.info(
        "Processing asteroid threat: {} (ID: {})",
        threatEvent.getName(),
        threatEvent.getAsteroidId());

    Optional<AsteroidAlertEntity> existingAlert =
        asteroidAlertRepository.findByAsteroidId(threatEvent.getAsteroidId());

    if (existingAlert.isPresent()) {
      log.info("Alert for asteroid {} already exists in database", threatEvent.getAsteroidId());
      return;
    }

    AsteroidAlertEntity alertEntity = mapToEntity(threatEvent);
    alertEntity = asteroidAlertRepository.save(alertEntity);
    log.info("Saved asteroid alert to database with ID: {}", alertEntity.getId());

    emailService.sendAsteroidAlert(alertEntity);

    alertEntity.setNotificationSent(true);
    asteroidAlertRepository.save(alertEntity);
  }

  private AsteroidAlertEntity mapToEntity(AsteroidThreatEvent dto) {
    return AsteroidAlertEntity.builder()
        .asteroidId(dto.getAsteroidId())
        .name(dto.getName())
        .estimatedDiameterMaxKm(dto.getEstimatedDiameterMaxKm())
        .isPotentiallyHazardous(dto.isPotentiallyHazardous())
        .closeApproachDate(dto.getCloseApproachDate())
        .missDistanceKm(dto.getMissDistanceKm())
        .relativeVelocityKmph(dto.getRelativeVelocityKmph())
        .nasaJplUrl(dto.getNasaJplUrl())
        .notificationSent(false)
        .build();
  }
}
