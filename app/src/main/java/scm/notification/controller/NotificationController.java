package scm.notification.controller;

import scm.notification.dto.NotificationRequest;
import scm.notification.dto.NotificationResponse;
import scm.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for notification operations.
 * Provides endpoints to trigger notifications across multiple channels.
 * 
 * API Version: v1
 */
@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Triggers a notification through the specified channel.
     * 
     * Supported channels:
     * - EMAIL: Sends email via SMTP
     * - SMS: Logs SMS (integration pending)
     * - WHATSAPP: Logs WhatsApp message (integration pending)
     * - PUSH: Logs push notification (integration pending)
     * 
     * @param request The notification request containing channel, recipient, and
     *                content
     * @return Response with notification ID and queued status
     */
    @PostMapping("/trigger")
    public ResponseEntity<NotificationResponse> triggerNotification(
            @Valid @RequestBody NotificationRequest request) {

        log.info("Received notification request for channel: {} to recipient: {}",
                request.getChannel(), request.getRecipient());

        NotificationResponse response = notificationService.trigger(request);

        log.info("Notification queued with ID: {}", response.getId());

        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint for load balancer probes.
     * 
     * @return OK status with simple health message
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Notification Service is healthy");
    }
}
