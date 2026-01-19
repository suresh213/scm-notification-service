package scm.notification.provider;

import scm.notification.entity.Notification;
import scm.notification.enums.NotificationChannel;

public interface NotificationProvider {
    boolean supports(NotificationChannel channel);

    void send(Notification notification);
}
