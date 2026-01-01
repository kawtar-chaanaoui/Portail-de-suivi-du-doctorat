package ma.emsi.notification_communication.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.notification_communication.Repositories.NotificationRepository;
import ma.emsi.notification_communication.dto.*;
import ma.emsi.notification_communication.entite.Notification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private static final String DEFAULT_NOTIFICATION_TYPE = "EMAIL";

    private final NotificationRepository notificationRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final EmailService emailService;
    private final NotificationComposer notificationComposer;
    private final PdfService pdfService;

    @Value("${app.kafka.topics.notifications:doctorate-notifications}")
    private String notificationTopic;

    public Notification queueNotification(NotificationRequest request) {
        NotificationContent content = notificationComposer.compose(request);

        String recipientEmail = request.resolveRecipientEmail();
        if (recipientEmail == null || recipientEmail.isBlank()) {
            throw new IllegalArgumentException("L'email destinataire est requis");
        }

        Notification notification = Notification.builder()
            .recipientEmail(recipientEmail)
            .recipientId(request.getRecipientId())
            .subject(content.getSubject())
            .content(content.getBody())
            .event(content.getEvent())
            .status(Notification.NotificationStatus.PENDING)
            .type(request.getChannel() != null ? request.getChannel() : DEFAULT_NOTIFICATION_TYPE)
            .build();

        Notification saved = request.isPersist() ? notificationRepository.save(notification) : notification;
        Long notificationId = request.isPersist() ? saved.getId() : null;

        NotificationMessage message = NotificationMessage.builder()
            .notificationId(notificationId)
            .recipientEmail(saved.getRecipientEmail())
            .recipientName(request.resolveRecipientName())
            .subject(content.getSubject())
            .body(content.getBody())
            .event(content.getEvent())
            .variables(content.getVariables())
            .cc(request.getCc())
            .bcc(request.getBcc())
            .pdf(request.getPdf())
            .attachments(request.getAttachments())
            .sendEmail(request.isSendEmail())
            .templateCode(content.getTemplateCode())
            .locale(request.getLocale())
            .scheduleAt(request.getScheduleAt())
            .build();

        kafkaTemplate.send(notificationTopic, message);
        log.info("Notification {} envoyée au topic {}", notificationId, notificationTopic);
        return saved;
    }

    @KafkaListener(topics = "${app.kafka.topics.notifications:doctorate-notifications}", 
                   containerFactory = "kafkaListenerContainerFactory")
    public void handleNotification(NotificationMessage message) {
        log.info("Consommation notification pour {}", message.getRecipientEmail());

        Notification notification = resolveOrCreateNotification(message);

        try {
            if (message.isSendEmail()) {
                Map<String, byte[]> attachments = buildAttachments(message);
                EmailPayload payload = EmailPayload.builder()
                    .to(message.getRecipientEmail())
                    .cc(message.getCc())
                    .bcc(message.getBcc())
                    .subject(message.getSubject())
                    .htmlBody(message.getBody())
                    .attachments(attachments)
                    .build();
                emailService.sendEmail(payload);
            }

            notification.setStatus(Notification.NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notification.setErrorMessage(null);
            log.info("Notification envoyée avec succès à {}", message.getRecipientEmail());
        } catch (Exception e) {
            log.error("Échec notification pour {} : {}", message.getRecipientEmail(), e.getMessage(), e);
            notification.setStatus(Notification.NotificationStatus.FAILED);
            notification.setErrorMessage(e.getMessage());
        }

        if (message.getNotificationId() != null || notification.getId() != null) {
            notificationRepository.save(notification);
        }
    }

    private Notification resolveOrCreateNotification(NotificationMessage message) {
        if (message.getNotificationId() != null) {
            return notificationRepository.findById(message.getNotificationId())
                .map(n -> {
                    n.setStatus(Notification.NotificationStatus.PROCESSING);
                    return notificationRepository.save(n);
                })
                .orElseGet(() -> createTransientNotification(message));
        }
        return createTransientNotification(message);
    }

    private Notification createTransientNotification(NotificationMessage message) {
        return Notification.builder()
            .recipientEmail(message.getRecipientEmail())
            .subject(message.getSubject())
            .content(message.getBody())
            .event(message.getEvent())
            .status(Notification.NotificationStatus.PROCESSING)
            .type(DEFAULT_NOTIFICATION_TYPE)
            .build();
    }

    private Map<String, byte[]> buildAttachments(NotificationMessage message) {
        Map<String, byte[]> attachments = new HashMap<>();
        if (message.getPdf() != null) {
            generatePdfAttachment(message.getPdf(), attachments);
        }
        if (!CollectionUtils.isEmpty(message.getAttachments())) {
            for (PdfGenerationRequest pdfRequest : message.getAttachments()) {
                generatePdfAttachment(pdfRequest, attachments);
            }
        }
        return attachments;
    }

    private void generatePdfAttachment(PdfGenerationRequest pdfRequest, Map<String, byte[]> attachments) {
        if (pdfRequest == null || pdfRequest.getDocumentType() == null) {
            return;
        }
        String documentType = pdfRequest.getDocumentType().toUpperCase();
        Map<String, Object> data = pdfRequest.getData() != null ? pdfRequest.getData() : Map.of();
        try {
            byte[] pdfBytes = switch (documentType) {
                case "ATTESTATION_INSCRIPTION" -> pdfService.generateAttestationInscription(
                    getStringValue(data, "studentName", ""),
                    getStringValue(data, "studentId", ""),
                    getStringValue(data, "academicYear", ""));
                case "AUTORISATION_SOUTENANCE" -> pdfService.generateAutorisationSoutenance(
                    getStringValue(data, "studentName", ""),
                    getStringValue(data, "thesisTitle", ""),
                    getStringValue(data, "soutenanceDate", ""));
                case "PROCES_VERBAL" -> pdfService.generateProcesVerbal(PdfMapper.fromMap(data));
                default -> throw new IllegalArgumentException("Type de document non supporté: " + documentType);
            };
            attachments.put(documentType.toLowerCase().replace("_", "-") + ".pdf", pdfBytes);
        } catch (Exception ex) {
            log.error("Impossible de générer le PDF {}: {}", documentType, ex.getMessage());
        }
    }

    private String getStringValue(Map<String, Object> map, String key, String defaultValue) {
        Object value = map.get(key);
        return value != null ? value.toString() : defaultValue;
    }

    public List<Notification> getUserNotifications(String userId) {
        if (userId == null || userId.isBlank()) {
            return List.of();
        }
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getNotificationsByEmail(String email) {
        if (email == null || email.isBlank()) {
            return List.of();
        }
        return notificationRepository.findByRecipientEmailOrderByCreatedAtDesc(email);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public Optional<Notification> getById(Long id) {
        return notificationRepository.findById(id);
    }

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @KafkaListener(
        topics = {"notifications-soutenance", "notifications-directeurs", "notifications-admin", "notifications-jury"},
        groupId = "soutenance-bridge-group",
        containerFactory = "stringKafkaListenerContainerFactory"
    )
    public void handleSoutenanceEvent(String payload) {
        log.info("Message Kafka Soutenance reçu : {}", payload);
        if (payload == null || payload.isBlank()) {
            return;
        }

        String[] parts = payload.split("\\|");
        String type = parts[0];
        Map<String, String> data = new HashMap<>();
        for (int i = 1; i < parts.length; i++) {
            String[] kv = parts[i].split(":", 2);
            if (kv.length == 2) {
                data.put(kv[0], kv[1]);
            }
        }

        NotificationRequest req = new NotificationRequest();
        req.setPersist(true);
        req.setSendEmail(false); // pour ne pas dépendre du SMTP en dev

        switch (type) {
            case "NOUVELLE_DEMANDE" -> {
                req.setEvent(Notification.NotificationEvent.SOUTENANCE_DEMANDEE);
                req.setStudentEmail(data.get("doctorantEmail"));
                req.setStudentName(data.get("doctorantNom"));
            }
            case "DEMANDE_A_VALIDER" -> {
                // "dossier soumis" côté CDC : informer le doctorant
                req.setEvent(Notification.NotificationEvent.SOUTENANCE_DEMANDEE);
                req.setStudentEmail(data.get("doctorantEmail"));
                req.setStudentName(data.get("doctorantNom"));
            }
            case "JURY_PROPOSE" -> {
                req.setEvent(Notification.NotificationEvent.JURY_PROPOSE);
            }
            case "SOUTENANCE_PLANIFIEE" -> {
                req.setEvent(Notification.NotificationEvent.SOUTENANCE_PLANIFIEE);
                req.setStudentEmail(data.get("doctorantEmail"));
                req.setStudentName(data.get("doctorantNom"));
                req.setDate(data.get("date"));
                req.setLieu(data.get("lieu"));
            }
            default -> {
                log.warn("Type de message Soutenance inconnu: {}", type);
                return;
            }
        }

        req.ensureVariables();
        req.getVariables().put("soutenanceId", data.get("soutenanceId"));
        req.getVariables().put("statut", data.get("statut"));
        req.getVariables().put("lieu", data.get("lieu"));

        // Si pas d'email, on ne persiste pas pour éviter une erreur
        if (req.resolveRecipientEmail() == null || req.resolveRecipientEmail().isBlank()) {
            log.warn("Aucun email destinataire dans le message Soutenance, notification ignorée");
            return;
        }

        queueNotification(req);
    }

    // Classe interne pour mapper les données PDF
    private static final class PdfMapper {
        private PdfMapper() {}

        static DocumentRequest fromMap(Map<String, Object> data) {
            DocumentRequest req = new DocumentRequest();
            if (data == null) return req;

            req.setStudentName(stringVal(data, "studentName"));
            req.setStudentId(stringVal(data, "studentId"));
            req.setAcademicYear(stringVal(data, "academicYear"));
            req.setThesisTitle(stringVal(data, "thesisTitle"));
            req.setDecision(stringVal(data, "decision"));
            req.setMention(stringVal(data, "mention"));

            Object soutenanceDate = data.get("soutenanceDateTime");
            if (soutenanceDate instanceof String str) {
                try {
                    req.setSoutenanceDateTime(LocalDateTime.parse(str, DateTimeFormatter.ISO_DATE_TIME));
                } catch (Exception ignored) {}
            }

            Object jury = data.get("juryMembers");
            if (jury instanceof List<?> list) {
                req.setJuryMembers(list.stream()
                    .filter(Map.class::isInstance)
                    .map(e -> (Map<?, ?>) e)
                    .map(m -> {
                        DocumentRequest.JuryMember member = new DocumentRequest.JuryMember();
                        member.setName(stringValRaw(m, "name"));
                        member.setRole(stringValRaw(m, "role"));
                        member.setInstitution(stringValRaw(m, "institution"));
                        return member;
                    })
                    .collect(Collectors.toList()));
            }

            Object obs = data.get("observations");
            if (obs instanceof List<?> obsList) {
                req.setObservations(obsList.stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .collect(Collectors.toList()));
            }
            return req;
        }

        private static String stringVal(Map<String, Object> map, String key) {
            Object v = map.get(key);
            return v != null ? v.toString() : null;
        }

        private static String stringValRaw(Map<?, ?> map, String key) {
            Object v = map.get(key);
            return v != null ? v.toString() : "";
        }
    }
}
