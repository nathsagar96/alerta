package dev.sagar.alerta.notification.repository;

import dev.sagar.alerta.notification.entity.AsteroidAlertEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsteroidAlertRepository extends JpaRepository<AsteroidAlertEntity, Long> {
  Optional<AsteroidAlertEntity> findByAsteroidId(String asteroidId);
}
