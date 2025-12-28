package ma.emsi.inscription_et_reinscription.controllers;

import ma.emsi.inscription_et_reinscription.dtos.TableauBordDTO;
import ma.emsi.inscription_et_reinscription.services.TableauBordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tableau-bord")
@CrossOrigin("*")
public class TableauBordController {

    @Autowired
    private TableauBordService tableauBordService;

    /**
     * Obtenir le tableau de bord complet d'un doctorant
     * Retourne : statut, étapes de validation, documents, alertes, dérogations
     */
    @GetMapping("/doctorant/{doctorantId}")
    public ResponseEntity<?> getTableauBord(@PathVariable Long doctorantId) {
        try {
            TableauBordDTO tableauBord = tableauBordService.getTableauBord(doctorantId);
            return ResponseEntity.ok(tableauBord);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
