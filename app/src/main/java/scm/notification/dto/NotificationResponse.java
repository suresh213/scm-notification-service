package scm.notification.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {
    private String id;
    private String status;
    private String message;
}
