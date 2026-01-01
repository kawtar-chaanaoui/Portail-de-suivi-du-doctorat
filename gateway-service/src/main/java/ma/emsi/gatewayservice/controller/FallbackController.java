package ma.emsi.gatewayservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller pour la page d'accueil et les fallbacks du Gateway
 */
@RestController
public class FallbackController {

    @GetMapping("/")
    public Mono<ResponseEntity<Map<String, Object>>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Gateway API - Portail de Suivi du Doctorat");
        response.put("status", "UP");
        response.put("message", "Bienvenue sur l'API Gateway");
        response.put("routes", Map.of(
            "soutenances", "/api/soutenances/**",
            "documents", "/api/documents/**",
            "jurys", "/api/jurys/**",
            "comptes", "/api/comptes/**",
            "inscriptions", "/api/inscriptions/**",
            "notifications", "/api/notifications/**"
        ));
        response.put("eureka", "http://localhost:8761");
        
        return Mono.just(ResponseEntity.ok(response));
    }

    @GetMapping("/fallback")
    public Mono<ResponseEntity<Map<String, String>>> fallback() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("message", "Le service demandé est temporairement indisponible. Veuillez réessayer plus tard.");
        
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response));
    }
}
