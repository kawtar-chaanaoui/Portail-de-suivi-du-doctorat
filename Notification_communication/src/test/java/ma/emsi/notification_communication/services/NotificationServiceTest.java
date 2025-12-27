package ma.emsi.notification_communication.services;

import ma.emsi.notification_communication.Repositories.NotificationRepository;
import ma.emsi.notification_communication.dto.NotificationContent;
import ma.emsi.notification_communication.dto.NotificationMessage;
import ma.emsi.notification_communication.dto.NotificationRequest;
import ma.emsi.notification_communication.entite.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Mock
    private EmailService emailService;
    @Mock
    private NotificationComposer notificationComposer;
    @Mock
    private PdfService pdfService;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificationService, "notificationTopic", "doctorate-notifications");
    }

    @Test
    void queueNotificationShouldPersistAndPublish() {
        NotificationRequest request = new NotificationRequest();
        request.setRecipientEmail("doctorant@emsi.ma");
        request.setVariables(new HashMap<>());

        NotificationContent content = NotificationContent.builder()
            .subject("Sujet test")
            .body("Contenu test")
            .variables(Map.of())
            .build();

        when(notificationComposer.compose(request)).thenReturn(content);
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification notification = invocation.getArgument(0);
            notification.setId(42L);
            return notification;
        });

        Notification saved = notificationService.queueNotification(request);

        assertThat(saved.getId()).isEqualTo(42L);
        assertThat(saved.getSubject()).isEqualTo("Sujet test");
        verify(notificationRepository).save(any(Notification.class));
        verify(kafkaTemplate).send(eq("doctorate-notifications"), any(NotificationMessage.class));
    }

    @Test
    void queueNotificationWithoutPersistenceShouldPublishWithNullId() {
        NotificationRequest request = new NotificationRequest();
        request.setRecipientEmail("doctorant@emsi.ma");
        request.setVariables(new HashMap<>());
        request.setPersist(false);

        NotificationContent content = NotificationContent.builder()
            .subject("Sujet")
            .body("Corps")
            .variables(Map.of())
            .build();

        when(notificationComposer.compose(request)).thenReturn(content);

        Notification saved = notificationService.queueNotification(request);
        assertThat(saved.getId()).isNull();

        ArgumentCaptor<NotificationMessage> captor = ArgumentCaptor.forClass(NotificationMessage.class);
        verify(kafkaTemplate).send(eq("doctorate-notifications"), captor.capture());
        assertThat(captor.getValue().getNotificationId()).isNull();
        verify(notificationRepository, times(0)).save(any(Notification.class));
    }

    @Test
    void handleNotificationShouldUpdateStatusAndSendEmail() {
        Notification notification = Notification.builder()
            .id(7L)
            .recipientEmail("doctorant@emsi.ma")
            .status(Notification.NotificationStatus.PENDING)
            .type("EMAIL")
            .build();

        when(notificationRepository.findById(7L)).thenReturn(Optional.of(notification));

        NotificationMessage message = NotificationMessage.builder()
            .notificationId(7L)
            .recipientEmail("doctorant@emsi.ma")
            .subject("Sujet")
            .body("<p>Corps</p>")
            .sendEmail(true)
            .build();

        notificationService.handleNotification(message);

        verify(emailService).sendEmail(any());
        verify(notificationRepository, times(2)).save(notification);
        assertThat(notification.getStatus()).isEqualTo(Notification.NotificationStatus.SENT);
        assertThat(notification.getSentAt()).isNotNull();
    }
}
