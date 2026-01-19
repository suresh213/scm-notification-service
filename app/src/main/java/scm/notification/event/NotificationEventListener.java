package scm.notification.event;

import scm.notification.service.NotificationProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationProcessor notificationProcessor;

    @EventListener
    public void handleNotificationCreated(NotificationCreatedEvent event) {
        // Trigger Async processing
        notificationProcessor.process(event.getNotificationId());
    }
}
