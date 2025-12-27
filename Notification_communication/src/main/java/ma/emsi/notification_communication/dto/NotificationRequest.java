package ma.emsi.notification_communication.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ma.emsi.notification_communication.entite.Notification;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationRequest {

    // Informations génériques destinataire / contenu
    private String recipientEmail;
    private String recipientName;
    private String subject;
    private String body;
    private Notification.NotificationEvent event;
    private String recipientId;
    private String channel;
    private String templateCode;
    private Map<String, Object> variables;
    private List<String> cc;
    private List<String> bcc;

    // Gestion de documents et pièces jointes
    private PdfGenerationRequest pdf;
    private List<PdfGenerationRequest> attachments;

    // Métadonnées supplémentaires
    private String locale;
    private boolean sendEmail = true;
    private boolean persist = true;
    private LocalDateTime scheduleAt;

    // Champs hérités pour compatibilité descendante (front actuel)
    private String studentEmail;
    private String studentName;
    private String deadline;
    private String date;
    private String heure;
    private String lieu;
    private String formation;
    private String heuresRestantes;

    public void ensureVariables() {
        if (variables == null) {
            variables = new HashMap<>();
        }
    }

    @JsonIgnore
    public String resolveRecipientEmail() {
        return recipientEmail != null ? recipientEmail : studentEmail;
    }

    @JsonIgnore
    public String resolveRecipientName() {
        return recipientName != null ? recipientName : studentName;
    }
}