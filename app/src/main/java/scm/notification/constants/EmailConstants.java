package scm.notification.constants;

/**
 * Production-grade email constants for the notification service.
 * Contains company branding, templates, and configuration values.
 */
public final class EmailConstants {

    private EmailConstants() {
        // Private constructor to prevent instantiation
    }

    // ============================================
    // COMPANY BRANDING
    // ============================================

    /**
     * Company name used in email headers and footers
     */
    public static final String COMPANY_NAME = "Your Company";

    /**
     * Company physical address for email footer (CAN-SPAM compliance)
     */
    public static final String COMPANY_ADDRESS = "123 Business Street, Tech City, TC 12345";

    /**
     * Company website URL
     */
    public static final String COMPANY_WEBSITE = "https://www.yourcompany.com";

    /**
     * Support email address
     */
    public static final String SUPPORT_EMAIL = "support@yourcompany.com";

    // ============================================
    // EMAIL TEMPLATES
    // ============================================

    /**
     * Base notification template path (Thymeleaf template)
     */
    public static final String TEMPLATE_BASE_NOTIFICATION = "email/base-notification";

    /**
     * Welcome email template
     */
    public static final String TEMPLATE_WELCOME = "email/welcome";

    /**
     * Password reset template
     */
    public static final String TEMPLATE_PASSWORD_RESET = "email/password-reset";

    /**
     * Order confirmation template
     */
    public static final String TEMPLATE_ORDER_CONFIRMATION = "email/order-confirmation";

    /**
     * Alert/notification template
     */
    public static final String TEMPLATE_ALERT = "email/alert";

    // ============================================
    // EMAIL CONFIGURATION
    // ============================================

    /**
     * Maximum retry attempts for failed email sending
     */
    public static final int MAX_RETRY_ATTEMPTS = 3;

    /**
     * Connection timeout in milliseconds
     */
    public static final int CONNECTION_TIMEOUT_MS = 5000;

    /**
     * Read timeout in milliseconds
     */
    public static final int READ_TIMEOUT_MS = 5000;

    /**
     * Write timeout in milliseconds
     */
    public static final int WRITE_TIMEOUT_MS = 5000;

    // ============================================
    // EMAIL SUBJECTS (Default templates)
    // ============================================

    /**
     * Default subject prefix for all emails
     */
    public static final String SUBJECT_PREFIX = "[Notification] ";

    /**
     * Default subject for general notifications
     */
    public static final String SUBJECT_GENERAL_NOTIFICATION = "You have a new notification";

    /**
     * Subject for welcome emails
     */
    public static final String SUBJECT_WELCOME = "Welcome to %s!";

    /**
     * Subject for password reset emails
     */
    public static final String SUBJECT_PASSWORD_RESET = "Password Reset Request";

    /**
     * Subject for alert emails
     */
    public static final String SUBJECT_ALERT = "Important Alert";

    // ============================================
    // CONTENT TYPE
    // ============================================

    /**
     * HTML content type
     */
    public static final String CONTENT_TYPE_HTML = "text/html; charset=UTF-8";

    /**
     * Plain text content type
     */
    public static final String CONTENT_TYPE_TEXT = "text/plain; charset=UTF-8";

    // ============================================
    // VALIDATION
    // ============================================

    /**
     * Email regex pattern for validation
     */
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    /**
     * Maximum subject length
     */
    public static final int MAX_SUBJECT_LENGTH = 255;

    /**
     * Maximum content length (in bytes)
     */
    public static final int MAX_CONTENT_LENGTH = 1048576; // 1MB
}
