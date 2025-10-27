package ma.emsi.notification_communication.controllers;
import lombok.RequiredArgsConstructor;
import ma.emsi.notification_communication.dto.ApiResponse;
import ma.emsi.notification_communication.dto.EmailRequest;
import ma.emsi.notification_communication.dto.NotificationRequest;
import ma.emsi.notification_communication.services.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {
    private final EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<ApiResponse> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendSimpleEmail(request.getTo(), request.getSubject(), request.getContent());
        return ResponseEntity.ok(ApiResponse.success("Email envoyé avec succès"));
    }

    @PostMapping("/dossier-soumis")
    public ResponseEntity<ApiResponse> notifyDossierSoumis(@RequestBody Map<String, String> data) {
        emailService.sendDossierSoumis(data.get("studentEmail"), data.get("studentName"));
        return ResponseEntity.ok(ApiResponse.success("Notification envoyée"));
    }

    @PostMapping("/dossier-valide")
    public ResponseEntity<ApiResponse> notifyDossierValide(@RequestBody NotificationRequest request) {
        emailService.sendDossierValide(request.getStudentEmail(), request.getStudentName());
        return ResponseEntity.ok(ApiResponse.success("Notification envoyée"));
    }

    @PostMapping("/rappel-reinscription")
    public ResponseEntity<ApiResponse> notifyRappelReinscription(@RequestBody NotificationRequest request) {
        emailService.sendRappelReinscription(
            request.getStudentEmail(), 
            request.getStudentName(),
            request.getDeadline()
        );
        return ResponseEntity.ok(ApiResponse.success("Rappel envoyé"));
    }

    @PostMapping("/reunion-jury")
    public ResponseEntity<ApiResponse> notifyReunionJury(@RequestBody NotificationRequest request) {
        emailService.sendNotificationReunionJury(
            request.getStudentEmail(),
            request.getStudentName(),
            request.getDate(),
            request.getHeure(),
            request.getLieu()
        );
        return ResponseEntity.ok(ApiResponse.success("Notification de réunion envoyée"));
    }

    @PostMapping("/rappel-formation")
    public ResponseEntity<ApiResponse> notifyFormationDoctorale(@RequestBody NotificationRequest request) {
        emailService.sendRappelFormationDoctorale(
            request.getStudentEmail(),
            request.getStudentName(),
            request.getFormation(),
            request.getHeuresRestantes()
        );
        return ResponseEntity.ok(ApiResponse.success("Rappel de formation envoyé"));
    }
}