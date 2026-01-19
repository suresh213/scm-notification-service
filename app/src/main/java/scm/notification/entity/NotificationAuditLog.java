package scm.notification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID notificationId;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String details;

    @CreationTimestamp
    private LocalDateTime timestamp;
}
