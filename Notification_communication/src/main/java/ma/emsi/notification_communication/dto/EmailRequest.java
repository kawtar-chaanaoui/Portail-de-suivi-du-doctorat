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
}