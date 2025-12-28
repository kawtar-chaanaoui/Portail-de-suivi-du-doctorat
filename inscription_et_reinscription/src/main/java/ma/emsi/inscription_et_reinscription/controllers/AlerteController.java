package ma.emsi.inscription_et_reinscription.controllers;

import ma.emsi.inscription_et_reinscription.dtos.AlerteDTO;
import ma.emsi.inscription_et_reinscription.entities.TypeAlerte;
import ma.emsi.inscription_et_reinscription.services.AlerteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertes")
@CrossOrigin("*")
public class AlerteController {

    @Autowired
    private AlerteService alerteService;

    /**
     * Créer une alerte manuelle
     */
    @PostMapping
    public ResponseEntity<?> creerAlerte(
            @RequestParam Long doctorantId,
            @RequestParam TypeAlerte type,
            @RequestParam String message) {
        try {
            AlerteDTO alerte = alerteService.creerAlerte(doctorantId, type, message);
            return ResponseEntity.ok(alerte);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtenir les alertes actives d'un doctorant
     */
    @GetMapping("/doctorant/{doctorantId}/actives")
    public ResponseEntity<List<AlerteDTO>> getAlertesActives(@PathVariable Long doctorantId) {
        try {
            List<AlerteDTO> alertes = alerteService.getAlertesActives(doctorantId);
            return ResponseEntity.ok(alertes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtenir toutes les alertes d'un doctorant
     */
    @GetMapping("/doctorant/{doctorantId}")
    public ResponseEntity<List<AlerteDTO>> getAllAlertes(@PathVariable Long doctorantId) {
        try {
            List<AlerteDTO> alertes = alerteService.getAllAlertes(doctorantId);
            return ResponseEntity.ok(alertes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Marquer une alerte comme traitée
     */
    @PutMapping("/{id}/traiter")
    public ResponseEntity<?> marquerAlerteTraitee(@PathVariable Long id) {
        try {
            alerteService.marquerAlerteTraitee(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Déclencher manuellement la vérification des alertes (pour test/admin)
     */
    @PostMapping("/verifier")
    public ResponseEntity<?> verifierEtGenererAlertes() {
        try {
            alerteService.verifierEtGenererAlertes();
            return ResponseEntity.ok("Vérification des alertes terminée");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
