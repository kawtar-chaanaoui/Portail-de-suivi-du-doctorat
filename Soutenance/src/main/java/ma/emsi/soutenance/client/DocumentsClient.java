package ma.emsi.soutenance.client;

import ma.emsi.soutenance.client.dto.DocumentSummaryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Client Feign pour récupérer des documents depuis un service externe.
 * Utilise gestion-comptes via Eureka ou URL directe.
 */
@FeignClient(
    name = "gestion-comptes",
    url = "${feign.client.documents.url:}",
    path = "/api/documents",
    fallback = DocumentsClientFallback.class
)
public interface DocumentsClient {

    @GetMapping("/external/soutenance/{soutenanceId}")
    List<DocumentSummaryDTO> getDocumentsBySoutenance(@PathVariable("soutenanceId") Long soutenanceId);
}
