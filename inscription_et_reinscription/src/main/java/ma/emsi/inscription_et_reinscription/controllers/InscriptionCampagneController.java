package ma.emsi.inscription_et_reinscription.controllers;

import ma.emsi.inscription_et_reinscription.dtos.CampagneDTO;
import ma.emsi.inscription_et_reinscription.services.InscriptionCampagneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campagnes")
@CrossOrigin("*")
public class InscriptionCampagneController {

    @Autowired
    private InscriptionCampagneService campagneService;

    @GetMapping
    public ResponseEntity<List<CampagneDTO>> getAllCampagnes() {
        return ResponseEntity.ok(campagneService.getAllCampagnes());
    }

    @GetMapping("/actives")
    public ResponseEntity<List<CampagneDTO>> getCampagnesActives() {
        return ResponseEntity.ok(campagneService.getCampagnesActives());
    }

    @GetMapping("/active/{type}")
    public ResponseEntity<?> getCampagneActiveByType(@PathVariable String type) {
        return campagneService.getCampagneActiveByType(type)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CampagneDTO> createCampagne(@RequestBody CampagneDTO campagneDTO) {
        CampagneDTO created = campagneService.createCampagne(campagneDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CampagneDTO> updateCampagne(@PathVariable Long id, @RequestBody CampagneDTO campagneDTO) {
        CampagneDTO updated = campagneService.updateCampagne(id, campagneDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactiverCampagne(@PathVariable Long id) {
        campagneService.desactiverCampagne(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{type}/est-active")
    public ResponseEntity<Boolean> isCampagneActive(@PathVariable String type) {
        return ResponseEntity.ok(campagneService.isCampagneActive(type));
    }
}