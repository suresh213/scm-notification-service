package scm.notification.service;

import scm.notification.dto.NotificationRequest;
import scm.notification.dto.NotificationResponse;
import scm.notification.entity.Notification;
import scm.notification.entity.NotificationAuditLog;
import scm.notification.enums.NotificationStatus;
import scm.notification.event.NotificationCreatedEvent;
import scm.notification.repository.NotificationAuditLogRepository;
import scm.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository repository;
    private final NotificationAuditLogRepository auditLogRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final NotificationProcessor notificationProcessor;

    @Transactional
    public NotificationResponse trigger(NotificationRequest request) {
        Notification notification = Notification.builder()
                .channel(request.getChannel())
                .recipient(request.getRecipient())
                .subject(request.getSubject())
                .content(request.getContent())
                .status(NotificationStatus.PENDING)
                .retryCount(0)
                .build();

        notification = repository.save(notification);
        logAudit(notification.getId(), "RECEIVED", "Notification request accepted");

        // Publish event to trigger async processing
        eventPublisher.publishEvent(new NotificationCreatedEvent(notification.getId()));

        return NotificationResponse.builder()
                .id(notification.getId().toString())
                .status("QUEUED")
                .message("Notification queued for delivery")
                .build();
    }

    @Scheduled(fixedRate = 60000) // Check every minute
    public void processPendingNotifications() {
        log.debug("Checking for pending/stuck notifications...");
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        List<NotificationStatus> statuses = Arrays.asList(NotificationStatus.PENDING, NotificationStatus.IN_PROGRESS);
        List<Notification> pending = repository.findPendingNotifications(statuses, tenMinutesAgo);

        if (pending.isEmpty()) {
            return;
        }

        log.info("Found {} stuck notifications to retry.", pending.size());
        for (Notification n : pending) {
            try {
                // Retrigger processing
                n.setRetryCount(n.getRetryCount() + 1);
                repository.save(n); // Increment count first
                logAudit(n.getId(), "RETRYING", "Retry attempt " + n.getRetryCount());

                // We call processor directly (it handles async internally)
                notificationProcessor.process(n.getId());
            } catch (Exception e) {
                log.error("Failed to process stuck notification {}", n.getId(), e);
            }
        }
    }

    @SuppressWarnings("null")
    private void logAudit(UUID notificationId, String status, String details) {
        NotificationAuditLog auditLog = NotificationAuditLog.builder()
                .notificationId(notificationId)
                .status(status)
                .details(details)
                .build();
        auditLogRepository.save(auditLog);
    }
}
