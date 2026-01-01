package ma.emsi.soutenance.controller;

import ma.emsi.soutenance.model.Soutenance;
import ma.emsi.soutenance.dtos.*;
import ma.emsi.soutenance.service.SoutenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/soutenances")
@RequiredArgsConstructor
public class SoutenanceController {

    private final SoutenanceService soutenanceService;

    // CdC: Formulaires de demande de soutenance initiés par le doctorant
    @PostMapping("/demande")
    @PreAuthorize("hasRole('DOCTORANT')")
    public ResponseEntity<Soutenance> creerDemande(@RequestBody DemandeSoutenanceDTO dto) {
        return ResponseEntity.ok(soutenanceService.creerDemandeSoutenance(dto));
    }

    @PostMapping("/{id}/soumettre")
    @PreAuthorize("hasRole('DOCTORANT')")
    public ResponseEntity<Soutenance> soumettreDemande(@PathVariable Long id) {
        return ResponseEntity.ok(soutenanceService.soumettreDemande(id));
    }

    @PostMapping("/{id}/verifier-prerequis")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> verifierPrerequis(@PathVariable Long id) {
        return ResponseEntity.ok(soutenanceService.verifierPrerequis(id));
    }

    // CdC: Autorisation de soutenance et planification administrative
    @PostMapping("/{id}/planifier")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Soutenance> planifierSoutenance(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date,
            @RequestParam String lieu,
            @RequestParam String salle) {
        return ResponseEntity.ok(soutenanceService.planifierSoutenance(id, date, lieu, salle));
    }

    @PostMapping("/{id}/terminer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Soutenance> terminerSoutenance(
            @PathVariable Long id,
            @RequestParam String procesVerbalPath) {
        return ResponseEntity.ok(soutenanceService.terminerSoutenance(id, procesVerbalPath));
    }

    // Consultation
    @GetMapping("/{id}")
    public ResponseEntity<Soutenance> getSoutenance(@PathVariable Long id) {
        return ResponseEntity.ok(soutenanceService.getById(id));
    }

    @GetMapping("/doctorant/{doctorantId}")
    @PreAuthorize("hasRole('DOCTORANT') or hasRole('ADMIN')")
    public ResponseEntity<List<Soutenance>> getByDoctorant(@PathVariable Long doctorantId) {
        return ResponseEntity.ok(soutenanceService.getByDoctorant(doctorantId));
    }

    @GetMapping("/directeur/{directeurId}")
    @PreAuthorize("hasRole('DIRECTEUR') or hasRole('ADMIN')")
    public ResponseEntity<List<Soutenance>> getByDirecteur(@PathVariable Long directeurId) {
        return ResponseEntity.ok(soutenanceService.getByDirecteur(directeurId));
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Soutenance>> getByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(soutenanceService.getByStatut(
                Soutenance.StatutSoutenance.valueOf(statut)));
    }
}