package ma.emsi.notification_communication.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.notification_communication.Repositories.EmailTemplateRepository;
import ma.emsi.notification_communication.entite.EmailTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final EmailTemplateRepository templateRepository;

    @Override
    public void run(String... args) {
        if (templateRepository.count() == 0) {
            log.info("Initialisation des templates email par défaut...");
            
            createTemplate("DOC_ENROL_SUBMITTED", "Dossier Soumis", 
                "Dossier de doctorat soumis - EMSI",
                """
                <html><body>
                <h2>Cher(e) ${studentName},</h2>
                <p>Votre dossier de doctorat a été soumis avec succès.</p>
                <p>Vous serez notifié dès qu'il sera validé par l'administration.</p>
                <br><p>Cordialement,<br>L'équipe Doctorat EMSI</p>
                </body></html>
                """);

            createTemplate("DOC_ENROL_APPROVED", "Dossier Validé",
                "Dossier de doctorat validé - EMSI",
                """
                <html><body>
                <h2>Cher(e) ${studentName},</h2>
                <p>Votre dossier de doctorat a été <strong>validé</strong>.</p>
                <p>Vous pouvez maintenant procéder aux étapes suivantes.</p>
                <br><p>Cordialement,<br>L'équipe Doctorat EMSI</p>
                </body></html>
                """);

            createTemplate("DOC_REENROL_REMINDER", "Rappel Réinscription",
                "Rappel de réinscription - Doctorat EMSI",
                """
                <html><body>
                <h2>Cher(e) ${studentName},</h2>
                <p>Rappel : la période de réinscription est en cours.</p>
                <p>Date limite : <strong>${deadline}</strong></p>
                <br><p>Cordialement,<br>L'équipe Doctorat EMSI</p>
                </body></html>
                """);

            createTemplate("DOC_DEFENSE_SCHEDULED", "Soutenance Planifiée",
                "Soutenance planifiée - EMSI",
                """
                <html><body>
                <h2>Cher(e) ${studentName},</h2>
                <p>Votre soutenance de thèse est programmée :</p>
                <ul>
                    <li>Date : <strong>${date}</strong></li>
                    <li>Heure : <strong>${heure}</strong></li>
                    <li>Lieu : <strong>${lieu}</strong></li>
                </ul>
                <br><p>Cordialement,<br>L'équipe Doctorat EMSI</p>
                </body></html>
                """);

            createTemplate("DOC_DEFENSE_AUTH", "Autorisation Soutenance",
                "Autorisation de soutenance - EMSI",
                """
                <html><body>
                <h2>Cher(e) ${studentName},</h2>
                <p>Vous êtes autorisé(e) à soutenir votre thèse de doctorat.</p>
                <p>Merci de prendre contact avec l'administration pour les modalités.</p>
                <br><p>Cordialement,<br>L'équipe Doctorat EMSI</p>
                </body></html>
                """);

            createTemplate("DOC_JURY_MEETING", "Réunion Jury",
                "Réunion du jury de thèse - EMSI",
                """
                <html><body>
                <h2>Cher(e) ${studentName},</h2>
                <p>La réunion de votre jury de thèse est programmée :</p>
                <ul>
                    <li>Date : <strong>${date}</strong></li>
                    <li>Heure : <strong>${heure}</strong></li>
                    <li>Lieu : <strong>${lieu}</strong></li>
                </ul>
                <br><p>Cordialement,<br>L'équipe Doctorat EMSI</p>
                </body></html>
                """);

            createTemplate("DOC_FORMATION_REMINDER", "Rappel Formation",
                "Formations doctorales - Rappel",
                """
                <html><body>
                <h2>Cher(e) ${studentName},</h2>
                <p>Il vous reste <strong>${heuresRestantes}</strong> heures de formation à valider.</p>
                <p>Formation proposée : ${formation}</p>
                <br><p>Cordialement,<br>L'équipe Doctorat EMSI</p>
                </body></html>
                """);

            log.info("Templates email initialisés avec succès.");
        }
    }

    private void createTemplate(String code, String name, String subject, String body) {
        EmailTemplate template = new EmailTemplate();
        template.setCode(code);
        template.setName(name);
        template.setSubject(subject);
        template.setBody(body);
        template.setActive(true);
        template.setDescription("Template auto-généré");
        templateRepository.save(template);
    }
}