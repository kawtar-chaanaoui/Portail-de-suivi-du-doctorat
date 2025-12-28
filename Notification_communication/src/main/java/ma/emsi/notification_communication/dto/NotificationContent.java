package ma.emsi.notification_communication.dto;

import lombok.Builder;
import lombok.Value;
import ma.emsi.notification_communication.entite.Notification;

import java.util.Map;

@Value
@Builder
public class NotificationContent {
    String subject;
    String body;
    String templateCode;
    Map<String, Object> variables;
    Notification.NotificationEvent event;
}
