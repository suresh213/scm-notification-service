package scm.notification.dto;

import scm.notification.enums.NotificationChannel;
import scm.notification.validation.ValidRecipient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@ValidRecipient
public class NotificationRequest {
    @NotNull(message = "Channel is required")
    private NotificationChannel channel;

    @NotBlank(message = "Recipient is required")
    private String recipient;

    private String subject;

    @NotBlank(message = "Content is required")
    private String content;
}
