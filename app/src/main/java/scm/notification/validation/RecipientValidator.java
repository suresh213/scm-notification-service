package scm.notification.validation;

import scm.notification.dto.NotificationRequest;
import scm.notification.enums.NotificationChannel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class RecipientValidator implements ConstraintValidator<ValidRecipient, NotificationRequest> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");

    @Override
    public boolean isValid(NotificationRequest request, ConstraintValidatorContext context) {
        if (request.getChannel() == null || request.getRecipient() == null) {
            return true; // Let @NotNull handle nulls
        }

        if (request.getChannel() == NotificationChannel.EMAIL) {
            return EMAIL_PATTERN.matcher(request.getRecipient()).matches();
        }

        if (request.getChannel() == NotificationChannel.SMS || request.getChannel() == NotificationChannel.WHATSAPP) {
            return PHONE_PATTERN.matcher(request.getRecipient()).matches();
        }

        return true; // Push tokens can be anything
    }
}
