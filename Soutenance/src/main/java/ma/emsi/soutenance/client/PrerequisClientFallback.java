package ma.emsi.soutenance.client;

import ma.emsi.soutenance.client.dto.PrerequisCheckResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PrerequisClientFallback implements PrerequisClient {

    private static final Logger log = LoggerFactory.getLogger(PrerequisClientFallback.class);

    @Override
    public PrerequisCheckResponse getPrerequisStatus(Long doctorantId) {
        log.warn("Feign fallback: impossible de récupérer le statut des prérequis pour le doctorant {}", doctorantId);
        return PrerequisCheckResponse.unavailable("Service prérequis indisponible");
    }
}
