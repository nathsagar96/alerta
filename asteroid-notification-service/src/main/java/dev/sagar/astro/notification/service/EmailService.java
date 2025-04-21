package dev.sagar.astro.notification.service;

import dev.sagar.astro.notification.entity.AsteroidAlertEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.text.DecimalFormat;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

  private final JavaMailSender mailSender;
  private final List<String> recipients;
  private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

  public EmailService(
      JavaMailSender mailSender,
      @Value("#{'${notification.email.to}'.split(',')}") List<String> recipients) {
    this.mailSender = mailSender;
    this.recipients = recipients;
  }

  public void sendAsteroidAlert(AsteroidAlertEntity asteroid) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setTo(recipients.toArray(new String[0]));
      helper.setSubject("ALERT: Potentially Hazardous Asteroid Detected - " + asteroid.getName());
      helper.setText(buildEmailContent(asteroid), true);

      mailSender.send(message);
      log.info("Asteroid alert email sent for: {}", asteroid.getName());
    } catch (MessagingException e) {
      log.error("Failed to send email alert for asteroid: {}", asteroid.getName(), e);
    }
  }

  private String buildEmailContent(AsteroidAlertEntity asteroid) {

    return "<html><body>"
        + "<h1 style='color: #c00;'>⚠️ Potentially Hazardous Asteroid Alert</h1>"
        + "<h2>"
        + asteroid.getName()
        + "</h2>"
        + "<h3>Asteroid Details:</h3>"
        + "<ul>"
        + "<li><strong>Asteroid ID:</strong> "
        + asteroid.getAsteroidId()
        + "</li>"
        + "<li><strong>Close Approach Date:</strong> "
        + asteroid.getCloseApproachDate()
        + "</li>"
        + "<li><strong>Estimated Max Diameter:</strong> "
        + decimalFormat.format(asteroid.getEstimatedDiameterMaxKm())
        + " km</li>"
        + "<li><strong>Miss Distance:</strong> "
        + decimalFormat.format(asteroid.getMissDistanceKm())
        + " km</li>"
        + "<li><strong>Relative Velocity:</strong> "
        + decimalFormat.format(asteroid.getRelativeVelocityKmph())
        + " km/h</li>"
        + "</ul>"
        + "<p>For more information, visit <a href='"
        + asteroid.getNasaJplUrl()
        + "'>NASA JPL Small-Body Database</a></p>"
        + "<p><em>This is an automated alert from the Asteroid Collision Notification System.</em></p>"
        + "</body></html>";
  }
}
