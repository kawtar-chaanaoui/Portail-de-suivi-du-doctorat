package ma.emsi.notification_communication.controllers;

import lombok.RequiredArgsConstructor;
import ma.emsi.notification_communication.dto.DocumentRequest;
import ma.emsi.notification_communication.services.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DocumentController {
    
    private final PdfService pdfService;

    @PostMapping("/attestation-inscription")
    public ResponseEntity<byte[]> generateAttestationInscription(@RequestBody DocumentRequest request) {
        byte[] pdf = pdfService.generateAttestationInscription(
            request.getStudentName(),
            request.getStudentId(),
            request.getAcademicYear()
        );

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attestation.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }

    @PostMapping("/autorisation-soutenance")
    public ResponseEntity<byte[]> generateAutorisationSoutenance(@RequestBody DocumentRequest request) {
        byte[] pdf = pdfService.generateAutorisationSoutenance(
            request.getStudentName(),
            request.getThesisTitle(),
            request.getSoutenanceDateTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=autorisation.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }

    @PostMapping("/proces-verbal")
    public ResponseEntity<byte[]> generateProcesVerbal(@RequestBody DocumentRequest request) {
        byte[] pdf = pdfService.generateProcesVerbal(request);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=proces-verbal.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }
}