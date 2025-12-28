package ma.emsi.notification_communication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.notification_communication.entite.Notification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationMessage {
    private Long notificationId;
    private String recipientEmail;
    private String recipientName;
    private String subject;
    private String body;
    private Notification.NotificationEvent event;
    private Map<String, Object> variables;
    private List<String> cc;
    private List<String> bcc;
    private PdfGenerationRequest pdf;
    private List<PdfGenerationRequest> attachments;
    private boolean sendEmail;
    private String templateCode;
    private String locale;
    private LocalDateTime scheduleAt;
}
