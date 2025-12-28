package ma.emsi.inscription_et_reinscription.controllers;

import ma.emsi.inscription_et_reinscription.dtos.DocumentDTO;
import ma.emsi.inscription_et_reinscription.entities.TypeDocument;
import ma.emsi.inscription_et_reinscription.exceptions.DocumentInvalideException;
import ma.emsi.inscription_et_reinscription.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin("*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/doctorant/{doctorantId}")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByDoctorant(@PathVariable Long doctorantId) {
        return ResponseEntity.ok(documentService.getDocumentsByDoctorant(doctorantId));
    }

    @GetMapping("/doctorant/{doctorantId}/type/{type}")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByDoctorantAndType(
            @PathVariable Long doctorantId,
            @PathVariable TypeDocument type) {

        return ResponseEntity.ok(documentService.getDocumentsByDoctorantAndType(doctorantId, type));
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long documentId) {
        try {
            byte[] content = documentService.getDocumentContent(documentId);
            DocumentDTO document = documentService.getDocumentsByDoctorant(null).stream()
                    .filter(d -> documentId.equals(d.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Document non trouvé"));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getNomFichier() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(content);

        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint pour valider un document avant upload (test)
     * Retourne les informations de validation
     */
    @PostMapping("/valider")
    public ResponseEntity<?> validerDocument(@RequestBody DocumentDTO documentDTO) {
        try {
            // Simuler la validation sans sauvegarder
            documentService.validerDocument(documentDTO);
            
            return ResponseEntity.ok(Map.of(
                    "valide", true,
                    "message", "Document valide",
                    "nomFichier", documentDTO.getNomFichier(),
                    "taille", documentDTO.getBase64Content() != null 
                        ? java.util.Base64.getDecoder().decode(documentDTO.getBase64Content()).length 
                        : 0
            ));
        } catch (DocumentInvalideException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "valide", false,
                    "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir les formats de fichiers acceptés
     */
    @GetMapping("/formats-acceptes")
    public ResponseEntity<?> getFormatsAcceptes() {
        return ResponseEntity.ok(Map.of(
                "formats", List.of("PDF", "JPG", "JPEG", "PNG"),
                "tailleMaximale", "10 MB",
                "tailleMaximaleBytes", 10485760,
                "description", "Formats acceptés : PDF pour les documents, JPG/JPEG/PNG pour les images"
        ));
    }
}