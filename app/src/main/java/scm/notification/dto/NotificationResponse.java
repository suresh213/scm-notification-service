package scm.notification.dto;

import scm.notification.enums.NotificationStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {
    private String id;
    private NotificationStatus status;
    private String message;
}
