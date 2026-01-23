package scm.notification.service;

import scm.notification.constants.EmailConstants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;
import org.thymeleaf.TemplateEngine;
import java.nio.charset.StandardCharsets;
import org.thymeleaf.context.Context;

import java.time.Year;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Production-grade email service for sending SMTP emails with HTML templates.
 * Supports both templated and plain-text emails.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("null")
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${notification.email.from}")
    private String fromEmail;

    @Value("${notification.email.from-name}")
    private String fromName;

    /**
     * Sends an email using the base notification template.
     *
     * @param to          Recipient email address
     * @param subject     Email subject
     * @param content     Email content (can contain HTML)
     * @param attachments Map of filename to content
     * @throws MessagingException if email sending fails
     */
    public void sendNotificationEmail(String to, String subject, String content, Map<String, String> attachments)
            throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("subject", subject != null ? subject : EmailConstants.SUBJECT_GENERAL_NOTIFICATION);
        variables.put("content", content);
        variables.put("recipientEmail", to);
        variables.put("companyName", EmailConstants.COMPANY_NAME);
        variables.put("companyAddress", EmailConstants.COMPANY_ADDRESS);
        variables.put("year", Year.now().getValue());

        sendTemplatedEmail(to, subject, EmailConstants.TEMPLATE_BASE_NOTIFICATION, variables, attachments);
    }

    /**
     * Sends an email using a custom Thymeleaf template.
     *
     * @param to           Recipient email address
     * @param subject      Email subject
     * @param templateName Template name (without .html extension)
     * @param variables    Template variables
     * @param attachments  Map of filename to content
     * @throws MessagingException if email sending fails
     */
    public void sendTemplatedEmail(String to, String subject, String templateName, Map<String, Object> variables,
            Map<String, String> attachments)
            throws MessagingException {

        log.info("[EMAIL SERVICE] Preparing templated email to: {}", to);

        try {
            // Create Thymeleaf context with variables
            Context context = new Context();
            context.setVariables(variables);

            // Process the template
            String htmlContent = templateEngine.process(templateName, context);

            // Create and send the email
            sendHtmlEmail(to, subject, htmlContent, attachments);

            log.info("[EMAIL SERVICE] Successfully sent templated email to: {}", to);

        } catch (Exception e) {
            log.error("[EMAIL SERVICE] Failed to process template '{}': {}", templateName, e.getMessage());
            throw new MessagingException("Failed to process email template: " + e.getMessage(), e);
        }
    }

    /**
     * Sends a raw HTML email without template processing.
     *
     * @param to          Recipient email address
     * @param subject     Email subject
     * @param htmlContent HTML content of the email
     * @param attachments Map of filename to content
     * @throws MessagingException if email sending fails
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent, Map<String, String> attachments)
            throws MessagingException {
        log.info("[EMAIL SERVICE] Sending HTML email to: {}", to);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName != null ? fromName : "Notification Service");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = isHtml

            if (attachments != null && !attachments.isEmpty()) {
                for (Map.Entry<String, String> entry : attachments.entrySet()) {
                    String filename = entry.getKey();
                    String content = entry.getValue();
                    if (content != null) {
                        byte[] fileBytes;
                        try {
                            // Strip whitespace to ensure standard Base64 parsing (MIME can be messy)
                            String sanitized = content.replaceAll("\\s+", "");
                            // Try to decode as Base64 (for images, pdfs, etc.)
                            fileBytes = Base64.getDecoder().decode(sanitized);
                        } catch (IllegalArgumentException e) {
                            // Fallback to plain text (for ics, txt, html files sent as raw string)
                            fileBytes = content.getBytes(StandardCharsets.UTF_8);
                        }
                        helper.addAttachment(filename, new ByteArrayResource(fileBytes));
                    }
                }
            }

            mailSender.send(message);

            log.info("[EMAIL SERVICE] HTML email sent successfully to: {}", to);

        } catch (MailException e) {
            log.error("[EMAIL SERVICE] Mail sending failed to {}: {}", to, e.getMessage());
            throw new MessagingException("Failed to send email: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("[EMAIL SERVICE] Unexpected error sending email to {}: {}", to, e.getMessage());
            throw new MessagingException("Unexpected error sending email: " + e.getMessage(), e);
        }
    }

    /**
     * Sends a plain text email.
     *
     * @param to          Recipient email address
     * @param subject     Email subject
     * @param textContent Plain text content
     * @throws MessagingException if email sending fails
     */
    public void sendPlainTextEmail(String to, String subject, String textContent) throws MessagingException {
        log.info("[EMAIL SERVICE] Sending plain text email to: {}", to);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(fromEmail, fromName != null ? fromName : "Notification Service");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(textContent, false); // false = not HTML

            mailSender.send(message);

            log.info("[EMAIL SERVICE] Plain text email sent successfully to: {}", to);

        } catch (MailException e) {
            log.error("[EMAIL SERVICE] Mail sending failed to {}: {}", to, e.getMessage());
            throw new MessagingException("Failed to send email: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("[EMAIL SERVICE] Unexpected error sending email to {}: {}", to, e.getMessage());
            throw new MessagingException("Unexpected error sending email: " + e.getMessage(), e);
        }
    }

    /**
     * Validates an email address format.
     *
     * @param email Email address to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return email.matches(EmailConstants.EMAIL_REGEX);
    }
}
