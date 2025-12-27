package ma.emsi.notification_communication.controllers;

import lombok.RequiredArgsConstructor;
import ma.emsi.notification_communication.dto.ApiResponse;
import ma.emsi.notification_communication.dto.EmailRequest;
import ma.emsi.notification_communication.dto.NotificationRequest;
import ma.emsi.notification_communication.entite.Notification;
import ma.emsi.notification_communication.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send-email")
    public ResponseEntity<ApiResponse> sendEmail(@RequestBody EmailRequest request) {
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setRecipientEmail(request.getTo());
        notificationRequest.setSubject(request.getSubject());
        notificationRequest.setBody(request.getContent());
        notificationRequest.setTemplateCode(request.getTemplateCode());
        notificationRequest.setVariables(request.getVariables());

        Notification saved = notificationService.queueNotification(notificationRequest);
        return ResponseEntity.ok(ApiResponse.success("Notification programmée", saved));
    }

    @PostMapping("/dossier-soumis")
    public ResponseEntity<ApiResponse> notifyDossierSoumis(@RequestBody Map<String, String> data) {
        NotificationRequest request = new NotificationRequest();
        request.setStudentEmail(data.get("email"));
        request.setStudentName(data.get("studentName"));
        request.setEvent(Notification.NotificationEvent.INSCRIPTION_SOUMISE);
        Notification saved = notificationService.queueNotification(request);
        return ResponseEntity.ok(ApiResponse.success("Notification mise en file", saved));
    }

    @PostMapping("/dossier-valide")
    public ResponseEntity<ApiResponse> notifyDossierValide(@RequestBody NotificationRequest request) {
        request.setEvent(Notification.NotificationEvent.INSCRIPTION_VALIDEE);
        Notification saved = notificationService.queueNotification(request);
        return ResponseEntity.ok(ApiResponse.success("Notification mise en file", saved));
    }

    @PostMapping("/rappel-reinscription")
    public ResponseEntity<ApiResponse> notifyRappelReinscription(@RequestBody NotificationRequest request) {
        request.setEvent(Notification.NotificationEvent.REINSCRIPTION_SOUMISE);
        Notification saved = notificationService.queueNotification(request);
        return ResponseEntity.ok(ApiResponse.success("Rappel planifié", saved));
    }

    @PostMapping("/reunion-jury")
    public ResponseEntity<ApiResponse> notifyReunionJury(@RequestBody NotificationRequest request) {
        request.setEvent(Notification.NotificationEvent.JURY_PROPOSE);
        Notification saved = notificationService.queueNotification(request);
        return ResponseEntity.ok(ApiResponse.success("Notification de réunion planifiée", saved));
    }

    @PostMapping("/soutenance-autorisee")
    public ResponseEntity<ApiResponse> notifySoutenanceAutorisee(@RequestBody NotificationRequest request) {
        request.setEvent(Notification.NotificationEvent.AUTORISATION_SOUTENANCE);
        Notification saved = notificationService.queueNotification(request);
        return ResponseEntity.ok(ApiResponse.success("Autorisation de soutenance notifiée", saved));
    }

    @PostMapping("/soutenance-planifiee")
    public ResponseEntity<ApiResponse> notifySoutenancePlanifiee(@RequestBody NotificationRequest request) {
        request.setEvent(Notification.NotificationEvent.SOUTENANCE_PLANIFIEE);
        Notification saved = notificationService.queueNotification(request);
        return ResponseEntity.ok(ApiResponse.success("Soutenance planifiée notifiée", saved));
    }

    @PostMapping("/rappel-formation")
    public ResponseEntity<ApiResponse> notifyFormationDoctorale(@RequestBody NotificationRequest request) {
        request.setEvent(Notification.NotificationEvent.PROGRESS_REPORT_DUE);
        Notification saved = notificationService.queueNotification(request);
        return ResponseEntity.ok(ApiResponse.success("Rappel de formation planifié", saved));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> customNotification(@RequestBody NotificationRequest request) {
        Notification saved = notificationService.queueNotification(request);
        return ResponseEntity.ok(ApiResponse.success("Notification en file", saved));
    }

    @GetMapping
    public ResponseEntity<List<Notification>> listNotifications(
            @RequestParam(required = false) String recipientId,
            @RequestParam(required = false) String email) {
        if (recipientId != null && !recipientId.isBlank()) {
            return ResponseEntity.ok(notificationService.getUserNotifications(recipientId));
        }
        if (email != null && !email.isBlank()) {
            return ResponseEntity.ok(notificationService.getNotificationsByEmail(email));
        }
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable Long id) {
        return notificationService.getById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marquée comme lue"));
    }
}