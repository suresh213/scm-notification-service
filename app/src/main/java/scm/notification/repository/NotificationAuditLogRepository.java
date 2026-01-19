package scm.notification.repository;

import scm.notification.entity.NotificationAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationAuditLogRepository extends JpaRepository<NotificationAuditLog, UUID> {
}
