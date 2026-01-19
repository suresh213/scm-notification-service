package scm.notification.service;

import scm.notification.entity.Notification;
import scm.notification.entity.NotificationAuditLog;
import scm.notification.enums.NotificationStatus;
import scm.notification.provider.NotificationProvider;
import scm.notification.provider.NotificationProviderFactory;
import scm.notification.repository.NotificationAuditLogRepository;
import scm.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.lang.NonNull;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationProcessor {

    private final NotificationRepository repository;
    private final NotificationAuditLogRepository auditLogRepository;
    private final NotificationProviderFactory providerFactory;

    private static final int MAX_RETRIES = 3;

    @Async("notificationExecutor")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void process(@NonNull UUID notificationId) {
        log.debug("Processing notification: {}", notificationId);

        Notification notification = repository.findById(notificationId).orElse(null);
        if (notification == null)
            return;

        // Optimistic locking or status check handling
        if (notification.getStatus() == NotificationStatus.SENT
                || notification.getStatus() == NotificationStatus.FAILED) {
            return;
        }

        try {
            // Mark as IN_PROGRESS if we want strict locking, but simplest is try to send
            notification.setStatus(NotificationStatus.IN_PROGRESS);
            notification = repository.saveAndFlush(notification); // Commit status change

            NotificationProvider provider = providerFactory.getProvider(notification.getChannel());
            provider.send(notification);

            notification.setStatus(NotificationStatus.SENT);
            notification.setErrorMessage(null);
            repository.save(notification);

            logAudit(notification.getId(), "SENT", "Successfully sent via " + notification.getChannel());

        } catch (Exception e) {
            log.error("Error sending notification {}: {}", notificationId, e.getMessage());
            handleFailure(notification, e.getMessage());
        }
    }

    private void handleFailure(Notification notification, String error) {
        notification.setErrorMessage(error);
        if (notification.getRetryCount() < MAX_RETRIES) {
            notification.setStatus(NotificationStatus.PENDING); // Back to PENDING for retry
            logAudit(notification.getId(), "ATTEMPT_FAILED", "Failed: " + error + ". Will retry.");
        } else {
            notification.setStatus(NotificationStatus.FAILED);
            logAudit(notification.getId(), "FAILED", "Max retries reached. Error: " + error);
        }
        repository.save(notification);
    }

    @SuppressWarnings("null")
    private void logAudit(UUID notificationId, String status, String details) {
        auditLogRepository.save(NotificationAuditLog.builder()
                .notificationId(notificationId)
                .status(status)
                .details(details)
                .build());
    }
}
