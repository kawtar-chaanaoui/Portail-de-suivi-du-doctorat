package ma.emsi.inscription_et_reinscription.controllers;



import ma.emsi.inscription_et_reinscription.dtos.ValidationDTO;
import ma.emsi.inscription_et_reinscription.entities.TypeValidation;
import ma.emsi.inscription_et_reinscription.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/validations")
@CrossOrigin("*")
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    @PostMapping("/directeur/{doctorantId}")
    public ResponseEntity<Void> validerDirecteur(
            @PathVariable Long doctorantId,
            @RequestBody Map<String, String> request) {

        Long validateurId = Long.parseLong(request.get("validateurId"));
        String validateurNom = request.get("validateurNom");
        String commentaire = request.get("commentaire");

        validationService.validerEtapeDirecteur(doctorantId, validateurId, validateurNom, commentaire);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/{doctorantId}")
    public ResponseEntity<Void> validerAdministratif(
            @PathVariable Long doctorantId,
            @RequestBody Map<String, String> request) {

        Long validateurId = Long.parseLong(request.get("validateurId"));
        String validateurNom = request.get("validateurNom");
        String commentaire = request.get("commentaire");

        validationService.validerEtapeAdministrative(doctorantId, validateurId, validateurNom, commentaire);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/rejet/{doctorantId}")
    public ResponseEntity<Void> rejeterValidation(
            @PathVariable Long doctorantId,
            @RequestBody Map<String, String> request) {

        TypeValidation type = TypeValidation.valueOf(request.get("type"));
        Long validateurId = Long.parseLong(request.get("validateurId"));
        String validateurNom = request.get("validateurNom");
        String commentaire = request.get("commentaire");

        validationService.rejeterEtape(doctorantId, type, validateurId, validateurNom, commentaire);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/doctorant/{doctorantId}")
    public ResponseEntity<List<ValidationDTO>> getValidationsByDoctorant(@PathVariable Long doctorantId) {
        return ResponseEntity.ok(validationService.getValidationsByDoctorant(doctorantId));
    }

    @GetMapping("/en-attente/{validateurId}")
    public ResponseEntity<List<ValidationDTO>> getValidationsEnAttente(
            @PathVariable Long validateurId,
            @RequestParam TypeValidation type) {

        return ResponseEntity.ok(validationService.getValidationsEnAttenteByValidateur(validateurId, type));
    }

    @GetMapping("/etape-actuelle/{doctorantId}")
    public ResponseEntity<ValidationDTO> getCurrentValidationEtape(@PathVariable Long doctorantId) {
        return validationService.getCurrentValidationEtape(doctorantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}