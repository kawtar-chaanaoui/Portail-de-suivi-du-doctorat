package ma.emsi.notification_communication.Repositories;

import ma.emsi.notification_communication.entite.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(String recipientId);
    List<Notification> findByRecipientEmailOrderByCreatedAtDesc(String recipientEmail);
    List<Notification> findByStatus(Notification.NotificationStatus status);
    List<Notification> findAllByOrderByCreatedAtDesc();
}