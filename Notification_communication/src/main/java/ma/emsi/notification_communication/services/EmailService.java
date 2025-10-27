package ma.emsi.notification_communication.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

// CORRECTION : Utiliser jakarta.mail au lieu de javax.mail
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom("noreply@doctorat.emsi.ma");

            mailSender.send(message);
            log.info("Email envoyé à: {}", to);

        } catch (MessagingException e) {
            log.error("Erreur envoi email: {}", e.getMessage());
            throw new RuntimeException("Erreur d'envoi d'email", e);
        }
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