package scm.notification.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class NotificationCreatedEvent {
    private final UUID notificationId;
}
