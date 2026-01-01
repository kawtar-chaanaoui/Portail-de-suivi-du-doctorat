package ma.emsi.soutenance.controller;

import ma.emsi.soutenance.model.Document;
import ma.emsi.soutenance.dtos.DocumentUploadDTO;
import ma.emsi.soutenance.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('DOCTORANT') or hasRole('DIRECTEUR') or hasRole('ADMIN')")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam Long soutenanceId,
            @RequestParam String typeDocument,
            @RequestParam MultipartFile fichier,
            @RequestParam String depotPar) throws IOException {

        return ResponseEntity.ok(documentService.uploadDocument(
                soutenanceId, typeDocument, fichier, depotPar));
    }

    @PostMapping("/{id}/valider")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Document> validerDocument(
            @PathVariable Long id,
            @RequestParam boolean valide,
            @RequestParam(required = false) String commentaire) {

        return ResponseEntity.ok(documentService.validerDocument(id, valide, commentaire));
    }

    @GetMapping("/soutenance/{soutenanceId}")
    public ResponseEntity<List<Document>> getDocumentsBySoutenance(
            @PathVariable Long soutenanceId) {

        return ResponseEntity.ok(documentService.getDocumentsBySoutenance(soutenanceId));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) throws IOException {
        Document document = documentService.getDocumentById(id);
        byte[] data = documentService.downloadDocument(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(document.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(document.getNomFichier())
                .build());

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}