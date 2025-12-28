package ma.emsi.notification_communication.controllers;

import lombok.RequiredArgsConstructor;
import ma.emsi.notification_communication.dto.ApiResponse;
import ma.emsi.notification_communication.entite.EmailTemplate;
import ma.emsi.notification_communication.services.TemplateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TemplateController {
    
    private final TemplateService templateService;

    @PostMapping
    public ResponseEntity<ApiResponse> createTemplate(@RequestBody EmailTemplate template) {
        EmailTemplate saved = templateService.saveTemplate(template);
        return ResponseEntity.ok(ApiResponse.success("Template créé avec succès"));
    }

    @GetMapping
    public ResponseEntity<List<EmailTemplate>> getAllTemplates() {
        return ResponseEntity.ok(templateService.getAllTemplates());
    }

    @GetMapping("/{name}")
    public ResponseEntity<EmailTemplate> getTemplateByName(@PathVariable String name) {
        return templateService.getTemplateByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.ok(ApiResponse.success("Template supprimé avec succès"));
    }
}