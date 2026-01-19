package scm.notification.provider;

import scm.notification.entity.Notification;
import scm.notification.enums.NotificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Push notification provider (placeholder implementation).
 * Currently logs the notification details for future integration with Firebase
 * FCM or Apple APNS.
 */
@Component
@Slf4j
public class PushNotificationProvider implements NotificationProvider {

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel == NotificationChannel.PUSH;
    }

    @Override
    public void send(Notification notification) {
        // TODO: Integrate with Firebase Cloud Messaging (FCM) or Apple Push
        // Notification Service (APNS)
        // This is a placeholder that logs the notification for future implementation

        log.info("╔══════════════════════════════════════════════════════════════════╗");
        log.info("║                      PUSH NOTIFICATION                            ║");
        log.info("╠══════════════════════════════════════════════════════════════════╣");
        log.info("║ Notification ID : {}                                            ", notification.getId());
        log.info("║ Device Token    : {}                                            ",
                truncateToken(notification.getRecipient()));
        log.info("║ Title           : {}                                            ", notification.getSubject());
        log.info("║ Body            : {}                                            ",
                truncateContent(notification.getContent()));
        log.info("║ Status          : LOGGED (Integration pending)                   ║");
        log.info("║ Timestamp       : {}                                            ", java.time.LocalDateTime.now());
        log.info("╚══════════════════════════════════════════════════════════════════╝");

        log.warn("[PUSH PROVIDER] Push notification integration not yet implemented. " +
                "Notification logged for device token: {}", truncateToken(notification.getRecipient()));
    }

    /**
     * Truncates device token for log display (for security and readability).
     */
    private String truncateToken(String token) {
        if (token == null)
            return "N/A";
        if (token.length() <= 20)
            return token;
        return token.substring(0, 10) + "..." + token.substring(token.length() - 10);
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
