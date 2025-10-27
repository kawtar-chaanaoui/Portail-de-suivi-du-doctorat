package ma.emsi.notification_communication.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PdfGenerationRequest {
    private String documentType;
    private Map<String, Object> data;
    private Long studentId;
    private Long thesisId;
}