package ma.emsi.soutenance.controller;

import ma.emsi.soutenance.model.Jury;
import ma.emsi.soutenance.dtos.PropositionJuryDTO;
import ma.emsi.soutenance.service.JuryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jurys")
@RequiredArgsConstructor
public class JuryController {

    private final JuryService juryService;

    // CdC: Proposition du jury par le Directeur de Thèse
    @PostMapping("/proposer")
    @PreAuthorize("hasRole('DIRECTEUR')")
    public ResponseEntity<Jury> proposerJury(@RequestBody PropositionJuryDTO dto) {
        return ResponseEntity.ok(juryService.proposerJury(dto));
    }

    @PostMapping("/{id}/valider")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Jury> validerJury(@PathVariable Long id) {
        return ResponseEntity.ok(juryService.validerJury(id));
    }

    @PostMapping("/membres/{id}/confirmer")
    public ResponseEntity<?> confirmerPresence(@PathVariable Long id) {
        return ResponseEntity.ok(juryService.confirmerPresence(id));
    }

    @GetMapping("/soutenance/{soutenanceId}")
    public ResponseEntity<Jury> getBySoutenance(@PathVariable Long soutenanceId) {
        return ResponseEntity.ok(juryService.getBySoutenance(soutenanceId));
    }

    @GetMapping("/directeur/{directeurId}")
    @PreAuthorize("hasRole('DIRECTEUR') or hasRole('ADMIN')")
    public ResponseEntity<List<Jury>> getByDirecteur(@PathVariable Long directeurId) {
        return ResponseEntity.ok(juryService.getByDirecteur(directeurId));
    }
}