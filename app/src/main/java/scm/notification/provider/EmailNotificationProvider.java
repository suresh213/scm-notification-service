package scm.notification.provider;

import scm.notification.constants.EmailConstants;
import scm.notification.entity.Notification;
import scm.notification.enums.NotificationChannel;
import scm.notification.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Production-grade email notification provider using SMTP.
 * Sends real emails via the configured SMTP server with HTML templates.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationProvider implements NotificationProvider {

    private final EmailService emailService;

    @Override
    public boolean supports(NotificationChannel channel) {
        return channel == NotificationChannel.EMAIL;
    }

    @Override
    public void send(Notification notification) {
        log.info("[EMAIL PROVIDER] Initiating email send to: {}", notification.getRecipient());

        String recipient = notification.getRecipient();
        String subject = notification.getSubject() != null
                ? notification.getSubject()
                : EmailConstants.SUBJECT_GENERAL_NOTIFICATION;
        String content = notification.getContent();

        // Validate email address
        if (!emailService.isValidEmail(recipient)) {
            throw new IllegalArgumentException("Invalid email address: " + recipient);
        }

        try {
            // Use the templated email service for production-grade emails
            emailService.sendNotificationEmail(recipient, subject, content);

            log.info("[EMAIL PROVIDER] Email successfully sent to: {}", recipient);

        } catch (MessagingException e) {
            log.error("[EMAIL PROVIDER] Failed to send email to {}: {}", recipient, e.getMessage(), e);
            throw new RuntimeException("Email sending failed: " + e.getMessage(), e);
        }
    }
}
