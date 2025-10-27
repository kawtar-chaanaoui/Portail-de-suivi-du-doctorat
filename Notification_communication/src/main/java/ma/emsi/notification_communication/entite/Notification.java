package ma.emsi.notification_communication.entite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // EMAIL, SYSTEM

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String recipientEmail;

    private String recipientId;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Enumerated(EnumType.STRING)
    private NotificationEvent event;

    private String errorMessage;

    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = NotificationStatus.PENDING;
        }
    }

    public enum NotificationStatus {
        PENDING, SENT, FAILED, DELIVERED
    }

    public enum NotificationEvent {
        INSCRIPTION_SOUMISE,
        INSCRIPTION_VALIDEE,
        INSCRIPTION_REJETEE,
        REINSCRIPTION_SOUMISE,
        SOUTENANCE_DEMANDEE,
        JURY_PROPOSE,
        AUTORISATION_SOUTENANCE,
        SOUTENANCE_PLANIFIEE
    }
}