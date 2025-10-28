package ma.emsi.notification_communication.dto;

import lombok.Data;
import java.util.Map;

@Data
public class EmailRequest {
    private String to;
    private String subject;
    private String content;
    private String templateCode;
    private Map<String, Object> variables;

    // Ajouter manuellement si Lombok ne fonctionne pas
    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}