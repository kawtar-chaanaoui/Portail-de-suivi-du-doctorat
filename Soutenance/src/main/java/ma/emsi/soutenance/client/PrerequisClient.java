package ma.emsi.soutenance.client;

import ma.emsi.soutenance.client.dto.PrerequisCheckResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Client Feign pour vérifier les prérequis d'un doctorant.
 * Utilise inscription_et_reinscription via Eureka ou URL directe.
 */
@FeignClient(
    name = "inscription-et-reinscription",
    url = "${feign.client.inscription.url:}",
    path = "/api/prerequis",
    fallback = PrerequisClientFallback.class
)
public interface PrerequisClient {

    @GetMapping("/doctorants/{doctorantId}/status")
    PrerequisCheckResponse getPrerequisStatus(@PathVariable("doctorantId") Long doctorantId);
}
