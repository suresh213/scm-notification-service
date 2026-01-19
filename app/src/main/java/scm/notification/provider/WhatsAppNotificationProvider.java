package scm.notification.provider;

import scm.notification.entity.Notification;
import scm.notification.enums.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * WhatsApp notification provider (placeholder implementation).
 * Currently logs the notification details for future integration with
 * Meta/Twilio WhatsApp API.
 */
@Component
@Slf4j
public class WhatsAppNotificationProvider implements NotificationProvider {

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel == NotificationChannel.WHATSAPP;
    }

    @Override
    public void send(Notification notification) {
        // TODO: Integrate with Meta Business API or Twilio WhatsApp API
        // This is a placeholder that logs the notification for future implementation

        log.info("╔══════════════════════════════════════════════════════════════════╗");
        log.info("║                    WHATSAPP NOTIFICATION                          ║");
        log.info("╠══════════════════════════════════════════════════════════════════╣");
        log.info("║ Notification ID : {}                                            ", notification.getId());
        log.info("║ Recipient       : {}                                            ", notification.getRecipient());
        log.info("║ Content         : {}                                            ",
                truncateContent(notification.getContent()));
        log.info("║ Status          : LOGGED (Integration pending)                   ║");
        log.info("║ Timestamp       : {}                                            ", java.time.LocalDateTime.now());
        log.info("╚══════════════════════════════════════════════════════════════════╝");

        log.warn("[WHATSAPP PROVIDER] WhatsApp integration not yet implemented. " +
                "Message logged for: {}", notification.getRecipient());
    }

    /**
     * Truncates content for log display to prevent overly long log lines.
     */
    private String truncateContent(String content) {
        if (content == null)
            return "N/A";
        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }
}
