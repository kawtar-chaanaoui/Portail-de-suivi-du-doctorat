package ma.emsi.soutenance.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentSummaryDTO {
    private Long id;
    private String typeDocument;
    private String statutValidation;
}
