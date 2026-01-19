package scm.notification.repository;

import scm.notification.entity.Notification;
import scm.notification.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("SELECT n FROM Notification n WHERE n.status IN :statuses AND n.updatedAt < :time")
    List<Notification> findPendingNotifications(@Param("statuses") List<NotificationStatus> statuses,
            @Param("time") LocalDateTime time);
}
