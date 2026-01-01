package ma.emsi.soutenance.client;

import ma.emsi.soutenance.client.dto.DocumentSummaryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DocumentsClientFallback implements DocumentsClient {

    private static final Logger log = LoggerFactory.getLogger(DocumentsClientFallback.class);

    @Override
    public List<DocumentSummaryDTO> getDocumentsBySoutenance(Long soutenanceId) {
        log.warn("Feign fallback: impossible de récupérer les documents pour la soutenance {}", soutenanceId);
        return Collections.emptyList();
    }
}
