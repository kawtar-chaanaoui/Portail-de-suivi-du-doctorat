package ma.emsi.soutenance.controller;


import ma.emsi.soutenance.model.Prerequis;
import ma.emsi.soutenance.service.PrerequisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/prerequis")
@RequiredArgsConstructor
public class PrerequisController {

    private final PrerequisService prerequisService;

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Prerequis> mettreAJourPrerequis(
            @PathVariable Long id,
            @RequestParam boolean verifie,
            @RequestParam String valeurActuelle,
            @RequestParam(required = false) String commentaire) {

        return ResponseEntity.ok(prerequisService.mettreAJourPrerequis(
                id, verifie, valeurActuelle, commentaire));
    }

    @GetMapping("/soutenance/{soutenanceId}")
    public ResponseEntity<List<Prerequis>> getBySoutenance(@PathVariable Long soutenanceId) {
        return ResponseEntity.ok(prerequisService.getPrerequisBySoutenance(soutenanceId));
    }

    @GetMapping("/soutenance/{soutenanceId}/valides")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> tousValides(@PathVariable Long soutenanceId) {
        return ResponseEntity.ok(prerequisService.tousPrerequisValides(soutenanceId));
    }
}