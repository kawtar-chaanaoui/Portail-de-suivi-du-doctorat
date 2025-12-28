package ma.emsi.notification_communication.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.notification_communication.dto.EmailPayload;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

// CORRECTION : Utiliser jakarta.mail au lieu de javax.mail
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String content) {
        sendEmail(EmailPayload.builder()
            .to(to)
            .subject(subject)
            .htmlBody(content)
            .build());
    }

    public void sendEmail(EmailPayload payload) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            boolean hasAttachments = payload.getAttachments() != null && !payload.getAttachments().isEmpty();
            MimeMessageHelper helper = new MimeMessageHelper(message, hasAttachments, "UTF-8");

            helper.setTo(payload.getTo());
            if (payload.getCc() != null && !payload.getCc().isEmpty()) {
                helper.setCc(payload.getCc().toArray(new String[0]));
            }
            if (payload.getBcc() != null && !payload.getBcc().isEmpty()) {
                helper.setBcc(payload.getBcc().toArray(new String[0]));
            }

            helper.setSubject(payload.getSubject());
            helper.setText(payload.getHtmlBody(), true);
            helper.setFrom("noreply@doctorat.emsi.ma");

            if (hasAttachments) {
                for (Map.Entry<String, byte[]> entry : payload.getAttachments().entrySet()) {
                    if (entry.getValue() != null) {
                        helper.addAttachment(entry.getKey(), new ByteArrayResource(entry.getValue()));
                    }
                }
            }

            mailSender.send(message);
            log.info("Email envoyé à: {}", payload.getTo());

        } catch (MessagingException e) {
            log.error("Erreur envoi email: {}", e.getMessage());
            throw new RuntimeException("Erreur d'envoi d'email", e);
        }
    }

    public void sendTestEmail(String to) {
        String subject = "Test Email Spring Boot";
        String content = "<html><body><h2>Ceci est un test d'envoi d'email depuis Spring Boot !</h2></body></html>";
        sendSimpleEmail(to, subject, content);
    }

    public void sendDossierSoumis(String studentEmail, String studentName) {
        String subject = "Dossier de thèse soumis - EMSI";
        String content = String.format("""
            <html>
            <body>
                <h2>Cher(e) %s,</h2>
                <p>Votre dossier de thèse a été soumis avec succès.</p>
                <p>Vous serez notifié dès qu'il sera validé.</p>
                <br>
                <p>Cordialement,<br>L'équipe Doctorat EMSI</p>
            </body>
            </html>
            """, studentName);

        sendSimpleEmail(studentEmail, subject, content);
    }

    public void sendDossierValide(String studentEmail, String studentName) {
        String subject = "Dossier de thèse validé - EMSI";
        String content = String.format("""
            <html>
            <body>
                <h2>Cher(e) %s,</h2>
                <p>Votre dossier de thèse a été validé avec succès.</p>
                <p>Vous pouvez maintenant procéder aux étapes suivantes du processus.</p>
                <br>
                <p>Cordialement,<br>L'équipe Doctorat EMSI</p>
            </body>
            </html>
            """, studentName);

        sendSimpleEmail(studentEmail, subject, content);
    }

    public void sendRappelReinscription(String studentEmail, String studentName, String deadline) {
        String subject = "Rappel de réinscription - Doctorat EMSI";
        String content = String.format("""
            <html>
            <body>
                <h2>Cher(e) %s,</h2>
                <p>Nous vous rappelons que la période de réinscription pour l'année doctorale est en cours.</p>
                <p>Merci de soumettre votre dossier de réinscription avant le : <strong>%s</strong></p>
                <p>Si vous rencontrez des difficultés, n'hésitez pas à nous contacter.</p>
                <br>
                <p>Cordialement,<br>L'équipe Doctorat EMSI</p>
            </body>
            </html>
            """, studentName, deadline);

        sendSimpleEmail(studentEmail, subject, content);
    }

    public void sendNotificationReunionJury(String studentEmail, String studentName, 
                                          String date, String heure, String lieu) {
        String subject = "Réunion du jury de thèse - EMSI";
        String content = String.format("""
            <html>
            <body>
                <h2>Cher(e) %s,</h2>
                <p>Nous vous informons que la réunion de votre jury de thèse est programmée :</p>
                <ul>
                    <li>Date : <strong>%s</strong></li>
                    <li>Heure : <strong>%s</strong></li>
                    <li>Lieu : <strong>%s</strong></li>
                </ul>
                <p>Votre présence est obligatoire.</p>
                <br>
                <p>Cordialement,<br>L'équipe Doctorat EMSI</p>
            </body>
            </html>
            """, studentName, date, heure, lieu);

        sendSimpleEmail(studentEmail, subject, content);
    }

    public void sendRappelFormationDoctorale(String studentEmail, String studentName, 
                                           String formation, String heuresRestantes) {
        String subject = "Formations Doctorales - Rappel";
        String content = String.format("""
            <html>
            <body>
                <h2>Cher(e) %s,</h2>
                <p>Nous vous rappelons qu'il vous reste <strong>%s heures</strong> de formation doctorale à effectuer.</p>
                <p>Une nouvelle session de formation est disponible :</p>
                <p><strong>%s</strong></p>
                <p>La validation de 200 heures de formation est requise pour la soutenance.</p>
                <br>
                <p>Cordialement,<br>L'équipe Doctorat EMSI</p>
            </body>
            </html>
            """, studentName, heuresRestantes, formation);

        sendSimpleEmail(studentEmail, subject, content);
    }
}