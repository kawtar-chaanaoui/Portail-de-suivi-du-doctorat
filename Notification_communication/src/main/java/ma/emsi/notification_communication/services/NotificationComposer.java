package ma.emsi.notification_communication.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.notification_communication.dto.NotificationContent;
import ma.emsi.notification_communication.dto.NotificationRequest;
import ma.emsi.notification_communication.entite.EmailTemplate;
import ma.emsi.notification_communication.entite.Notification;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationComposer {

    private static final Map<Notification.NotificationEvent, String> DEFAULT_TEMPLATE_CODES = Map.ofEntries(
        Map.entry(Notification.NotificationEvent.INSCRIPTION_SOUMISE, "DOC_ENROL_SUBMITTED"),
        Map.entry(Notification.NotificationEvent.INSCRIPTION_VALIDEE, "DOC_ENROL_APPROVED"),
        Map.entry(Notification.NotificationEvent.REINSCRIPTION_SOUMISE, "DOC_REENROL_REMINDER"),
        Map.entry(Notification.NotificationEvent.SOUTENANCE_PLANIFIEE, "DOC_DEFENSE_SCHEDULED"),
        Map.entry(Notification.NotificationEvent.AUTORISATION_SOUTENANCE, "DOC_DEFENSE_AUTH"),
        Map.entry(Notification.NotificationEvent.SOUTENANCE_DEMANDEE, "DOC_DEFENSE_REQUEST"),
        Map.entry(Notification.NotificationEvent.JURY_PROPOSE, "DOC_JURY_MEETING"),
        Map.entry(Notification.NotificationEvent.PROCESS_COMPLETE, "DOC_PROCESS_COMPLETE"),
        Map.entry(Notification.NotificationEvent.PROGRESS_REPORT_DUE, "DOC_FORMATION_REMINDER")
    );

    private final TemplateService templateService;

    public NotificationContent compose(NotificationRequest request) {
        request.ensureVariables();
        Map<String, Object> variables = new HashMap<>(request.getVariables());
        variables.putIfAbsent("studentName", request.resolveRecipientName());
        variables.putIfAbsent("studentEmail", request.resolveRecipientEmail());
        variables.putIfAbsent("deadline", request.getDeadline());
        variables.putIfAbsent("date", request.getDate());
        variables.putIfAbsent("heure", request.getHeure());
        variables.putIfAbsent("lieu", request.getLieu());
        variables.putIfAbsent("formation", request.getFormation());
        variables.putIfAbsent("heuresRestantes", request.getHeuresRestantes());

        String subject = request.getSubject();
        String body = request.getBody();
        String templateCode = resolveTemplateCode(request);

        if (templateCode != null) {
            Optional<EmailTemplate> templateOpt = templateService.getActiveTemplateByCode(templateCode);
            if (templateOpt.isPresent()) {
                EmailTemplate template = templateOpt.get();
                subject = subject != null ? subject : template.getSubject();
                body = templateService.processTemplate(template.getBody(), variables);
            } else {
                log.warn("Template {} introuvable ou inactif, fallback sur contenu brut", templateCode);
            }
        }

        if (subject == null) {
            subject = buildFallbackSubject(request);
        }
        if (body == null) {
            body = buildFallbackBody(request, variables);
        }

        return NotificationContent.builder()
            .subject(subject)
            .body(body)
            .templateCode(templateCode)
            .variables(variables)
            .event(request.getEvent())
            .build();
    }

    private String resolveTemplateCode(NotificationRequest request) {
        if (request.getTemplateCode() != null && !request.getTemplateCode().isBlank()) {
            return request.getTemplateCode();
        }
        if (request.getEvent() != null) {
            return DEFAULT_TEMPLATE_CODES.get(request.getEvent());
        }
        return null;
    }

    private String buildFallbackSubject(NotificationRequest request) {
        if (request.getEvent() == null) {
            return "Notification";
        }
        return switch (request.getEvent()) {
            case INSCRIPTION_SOUMISE -> "Réception du dossier";
            case INSCRIPTION_VALIDEE -> "Validation du dossier";
            case INSCRIPTION_REJETEE -> "Dossier en attente";
            case REINSCRIPTION_SOUMISE -> "Rappel de réinscription";
            case SOUTENANCE_DEMANDEE -> "Demande de soutenance";
            case AUTORISATION_SOUTENANCE -> "Autorisation de soutenance";
            case SOUTENANCE_PLANIFIEE -> "Soutenance planifiée";
            case JURY_PROPOSE -> "Réunion du jury";
            case PROCESS_COMPLETE -> "Processus finalisé";
            case PROGRESS_REPORT_DUE -> "Rappel de formation";
            default -> "Notification doctorat";
        };
    }

    private String buildFallbackBody(NotificationRequest request, Map<String, Object> variables) {
        String recipient = request.resolveRecipientName() != null ? request.resolveRecipientName() : "Cher doctorant";
        if (request.getEvent() == null) {
            return "Bonjour,\n\nCette notification est générée par la plateforme doctorale.\n\nCordialement.";
        }
        return switch (request.getEvent()) {
            case INSCRIPTION_SOUMISE -> String.format("Bonjour %s,\n\nVotre dossier a été soumis avec succès.\nNous vous informerons lors de la validation.\n\nCordialement.", recipient);
            case INSCRIPTION_VALIDEE -> String.format("Bonjour %s,\n\nVotre dossier a été validé. Vous pouvez poursuivre les étapes suivantes.\n\nCordialement.", recipient);
            case INSCRIPTION_REJETEE -> String.format("Bonjour %s,\n\nVotre dossier nécessite des compléments. Merci de consulter la plateforme.\n\nCordialement.", recipient);
            case REINSCRIPTION_SOUMISE -> String.format("Bonjour %s,\n\nRappel : merci de finaliser votre réinscription avant le %s.\n\nCordialement.", recipient, variables.getOrDefault("deadline", "la date limite"));
            case SOUTENANCE_DEMANDEE -> String.format("Bonjour %s,\n\nVotre demande de soutenance a été enregistrée.\nNous vous contacterons pour la suite.\n\nCordialement.", recipient);
            case AUTORISATION_SOUTENANCE -> String.format("Bonjour %s,\n\nVous êtes autorisé à soutenir votre thèse.\n\nCordialement.", recipient);
            case SOUTENANCE_PLANIFIEE -> String.format("Bonjour %s,\n\nVotre soutenance est programmée le %s à %s au %s.\n\nCordialement.",
                recipient,
                variables.getOrDefault("date", "date à confirmer"),
                variables.getOrDefault("heure", "horaire à confirmer"),
                variables.getOrDefault("lieu", "lieu à confirmer"));
            case JURY_PROPOSE -> String.format("Bonjour %s,\n\nLa réunion du jury est prévue le %s à %s.\nLieu : %s.\n\nCordialement.",
                recipient,
                variables.getOrDefault("date", "date à définir"),
                variables.getOrDefault("heure", "heure à définir"),
                variables.getOrDefault("lieu", "à préciser"));
            case PROCESS_COMPLETE -> String.format("Bonjour %s,\n\nToutes les étapes de votre processus doctorale sont finalisées.\n\nCordialement.", recipient);
            case PROGRESS_REPORT_DUE -> String.format("Bonjour %s,\n\nIl vous reste %s heures de formation à valider.\nFormation proposée : %s.\n\nCordialement.",
                recipient,
                variables.getOrDefault("heuresRestantes", "des heures"),
                variables.getOrDefault("formation", "Formation doctorale"));
            default -> String.format("Bonjour %s,\n\nVous avez reçu une nouvelle notification concernant votre doctorat.\n\nCordialement.", recipient);
        };
    }
}
