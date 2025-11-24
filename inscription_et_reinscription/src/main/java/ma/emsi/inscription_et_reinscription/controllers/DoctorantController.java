package ma.emsi.inscription_et_reinscription.controllers;

import ma.emsi.inscription_et_reinscription.dtos.DoctorantDTO;
import ma.emsi.inscription_et_reinscription.dtos.InscriptionRequestDTO;
import ma.emsi.inscription_et_reinscription.entities.StatutInscription;
import ma.emsi.inscription_et_reinscription.services.DoctorantService;
import ma.emsi.inscription_et_reinscription.services.UserIntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctorants")
@CrossOrigin("*")
public class DoctorantController {

    @Autowired
    private DoctorantService doctorantService;

    @Autowired
    private UserIntegrationService userIntegrationService;

    @PostMapping("/inscription")
    public ResponseEntity<?> soumettreInscription(@RequestBody InscriptionRequestDTO request) {
        try {
            DoctorantDTO doctorant = doctorantService.soumettreInscription(request);
            return ResponseEntity.ok(doctorant);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/reinscription")
    public ResponseEntity<?> demanderReinscription(@PathVariable Long id) {
        try {
            DoctorantDTO doctorant = doctorantService.demanderReinscription(id);
            return ResponseEntity.ok(doctorant);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorantDTO> getDoctorant(@PathVariable Long id) {
        return doctorantService.getDoctorantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DoctorantDTO> getDoctorantByUserId(@PathVariable Long userId) {
        return doctorantService.getDoctorantByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cin/{cin}")
    public ResponseEntity<DoctorantDTO> getDoctorantByCin(@PathVariable String cin) {
        return doctorantService.getDoctorantByCin(cin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<DoctorantDTO>> getDoctorantsByStatut(@PathVariable StatutInscription statut) {
        List<DoctorantDTO> doctorants = doctorantService.getDoctorantsByStatut(statut);
        return ResponseEntity.ok(doctorants);
    }

    @GetMapping("/directeur/{directeurUserId}")
    public ResponseEntity<List<DoctorantDTO>> getDoctorantsByDirecteur(@PathVariable Long directeurUserId) {
        List<DoctorantDTO> doctorants = doctorantService.getDoctorantsByDirecteur(directeurUserId);
        return ResponseEntity.ok(doctorants);
    }

    @GetMapping("/directeurs")
    public ResponseEntity<?> getDirecteurs() {
        try {
            // Simulation - à remplacer par appel réel au module user
            return ResponseEntity.ok(userIntegrationService.getDirecteurs("simulated-token"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur lors de la récupération des directeurs");
        }
    }
}