package scm.notification.provider;

import scm.notification.entity.Notification;
import scm.notification.enums.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * SMS notification provider (placeholder implementation).
 * Currently logs the notification details for future integration with
 * Twilio/MessageBird API.
 */
@Component
@Slf4j
public class SmsNotificationProvider implements NotificationProvider {

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel == NotificationChannel.SMS;
    }

    @Override
    public void send(Notification notification) {
        // TODO: Integrate with Twilio, MessageBird, or AWS SNS
        // This is a placeholder that logs the notification for future implementation

        log.info("╔══════════════════════════════════════════════════════════════════╗");
        log.info("║                       SMS NOTIFICATION                            ║");
        log.info("╠══════════════════════════════════════════════════════════════════╣");
        log.info("║ Notification ID : {}                                            ", notification.getId());
        log.info("║ Phone Number    : {}                                            ", notification.getRecipient());
        log.info("║ Message         : {}                                            ",
                truncateContent(notification.getContent()));
        log.info("║ Status          : LOGGED (Integration pending)                   ║");
        log.info("║ Timestamp       : {}                                            ", java.time.LocalDateTime.now());
        log.info("╚══════════════════════════════════════════════════════════════════╝");

        log.warn("[SMS PROVIDER] SMS integration not yet implemented. " +
                "Message logged for phone: {}", notification.getRecipient());
    }

    /**
     * Truncates content for log display to prevent overly long log lines.
     */
    private String truncateContent(String content) {
        if (content == null)
            return "N/A";
        return content.length() > 100 ? content.substring(0, 100) + "..." : content;
    }
}
