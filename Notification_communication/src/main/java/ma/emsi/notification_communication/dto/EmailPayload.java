package ma.emsi.notification_communication.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class EmailPayload {
    String to;
    List<String> cc;
    List<String> bcc;
    String subject;
    String htmlBody;
    Map<String, byte[]> attachments;
}
