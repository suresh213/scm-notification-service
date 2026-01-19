package scm.notification.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class NotificationCreatedEvent {
    @NonNull
    private final UUID notificationId;
}
