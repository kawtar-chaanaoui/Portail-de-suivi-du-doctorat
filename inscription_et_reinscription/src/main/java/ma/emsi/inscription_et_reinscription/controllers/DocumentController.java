package ma.emsi.inscription_et_reinscription.controllers;



import ma.emsi.inscription_et_reinscription.dtos.DocumentDTO;
import ma.emsi.inscription_et_reinscription.entities.TypeDocument;
import ma.emsi.inscription_et_reinscription.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
}