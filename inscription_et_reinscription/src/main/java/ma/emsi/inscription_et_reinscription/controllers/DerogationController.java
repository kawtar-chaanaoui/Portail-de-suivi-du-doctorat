package ma.emsi.inscription_et_reinscription.controllers;

import ma.emsi.inscription_et_reinscription.dtos.DerogationDTO;
import ma.emsi.inscription_et_reinscription.services.DerogationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/derogations")
@CrossOrigin("*")
public class DerogationController {

    @Autowired
    private DerogationService derogationService;

    /**
     * Demander une dérogation
     */
    @PostMapping("/demander")
    public ResponseEntity<?> demanderDerogation(
            @RequestParam Long doctorantId,
            @RequestParam String motif) {
        try {
            DerogationDTO derogation = derogationService.demanderDerogation(doctorantId, motif);
            return ResponseEntity.ok(derogation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Valider une dérogation (PED)
     */
    @PostMapping("/{id}/valider")
    public ResponseEntity<?> validerDerogation(
            @PathVariable Long id,
            @RequestParam Long validateurPedId,
            @RequestParam String validateurPedNom,
            @RequestParam(required = false) String commentaire) {
        try {
            DerogationDTO derogation = derogationService.validerDerogation(
                    id, validateurPedId, validateurPedNom, commentaire);
            return ResponseEntity.ok(derogation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Rejeter une dérogation (PED)
     */
    @PostMapping("/{id}/rejeter")
    public ResponseEntity<?> rejeterDerogation(
            @PathVariable Long id,
            @RequestParam Long validateurPedId,
            @RequestParam String validateurPedNom,
            @RequestParam String commentaire) {
        try {
            DerogationDTO derogation = derogationService.rejeterDerogation(
                    id, validateurPedId, validateurPedNom, commentaire);
            return ResponseEntity.ok(derogation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtenir les dérogations d'un doctorant
     */
    @GetMapping("/doctorant/{doctorantId}")
    public ResponseEntity<List<DerogationDTO>> getDerogationsByDoctorant(@PathVariable Long doctorantId) {
        try {
            List<DerogationDTO> derogations = derogationService.getDerogationsByDoctorant(doctorantId);
            return ResponseEntity.ok(derogations);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtenir toutes les dérogations en attente (pour le PED)
     */
    @GetMapping("/en-attente")
    public ResponseEntity<List<DerogationDTO>> getDerogationsEnAttente() {
        List<DerogationDTO> derogations = derogationService.getDerogationsEnAttente();
        return ResponseEntity.ok(derogations);
    }
}
