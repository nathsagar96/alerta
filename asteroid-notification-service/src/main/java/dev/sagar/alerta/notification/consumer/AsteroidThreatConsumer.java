package dev.sagar.alerta.notification.consumer;

import dev.sagar.alerta.notification.dto.AsteroidThreatEvent;
import dev.sagar.alerta.notification.service.NotificationHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsteroidThreatConsumer {

  private final NotificationHandlerService notificationHandlerService;

  @KafkaListener(
      topics = "${kafka.topic.asteroid-threats}",
      groupId = "${spring.kafka.consumer.group-id}",
      containerFactory = "kafkaListenerContainerFactory")
  public void consumeAsteroidThreat(
      @Payload AsteroidThreatEvent threatEvent,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
      @Header(KafkaHeaders.OFFSET) long offset) {

    log.info(
        "Received asteroid threat event from topic {}, partition {}, offset {}: {} (ID: {})",
        topic,
        partition,
        offset,
        threatEvent.getName(),
        threatEvent.getAsteroidId());

    try {
      notificationHandlerService.processAsteroidThreat(threatEvent);
    } catch (Exception e) {
      log.error("Error processing asteroid threat event: {}", threatEvent.getAsteroidId(), e);
    }
  }
}
