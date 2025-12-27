package ma.emsi.notification_communication.services;

import ma.emsi.notification_communication.dto.NotificationContent;
import ma.emsi.notification_communication.dto.NotificationRequest;
import ma.emsi.notification_communication.entite.EmailTemplate;
import ma.emsi.notification_communication.entite.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationComposerTest {

    @Mock
    private TemplateService templateService;

    @InjectMocks
    private NotificationComposer notificationComposer;

    @BeforeEach
    void init() {
        notificationComposer = new NotificationComposer(templateService);
    }

    @Test
    void composeShouldProvideFallbackContentWhenTemplateMissing() {
        NotificationRequest request = new NotificationRequest();
        request.setStudentEmail("doctorant@emsi.ma");
        request.setStudentName("Meryem");
        request.setEvent(Notification.NotificationEvent.INSCRIPTION_SOUMISE);

        NotificationContent content = notificationComposer.compose(request);

        assertThat(content.getSubject()).isEqualTo("Réception du dossier");
        assertThat(content.getBody()).contains("Votre dossier a été soumis");
        assertThat(content.getVariables()).containsEntry("studentName", "Meryem");
    }

    @Test
    void composeShouldUseTemplateWhenAvailable() {
        EmailTemplate template = new EmailTemplate();
        template.setSubject("Notification personnalisée");
        template.setBody("Bonjour ${studentName}, bienvenue");

        when(templateService.getActiveTemplateByCode("CUSTOM_CODE")).thenReturn(Optional.of(template));
        when(templateService.processTemplate(eq("Bonjour ${studentName}, bienvenue"), anyMap())).thenReturn("Bonjour Yassine, bienvenue");

        NotificationRequest request = new NotificationRequest();
        request.setTemplateCode("CUSTOM_CODE");
        request.setRecipientName("Yassine");
        request.setRecipientEmail("yassine@emsi.ma");
        request.setVariables(Map.of("studentName", "Yassine"));

        NotificationContent content = notificationComposer.compose(request);

        assertThat(content.getSubject()).isEqualTo("Notification personnalisée");
        assertThat(content.getBody()).isEqualTo("Bonjour Yassine, bienvenue");
        assertThat(content.getTemplateCode()).isEqualTo("CUSTOM_CODE");
    }
}
