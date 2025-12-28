package ma.emsi.notification_communication.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationRequest {
    private String studentEmail;
    private String studentName;
    
    // Pour les rappels de réinscription
    private String deadline;
    
    // Pour les réunions du jury
    private String date;
    private String heure;
    private String lieu;
    
    // Pour les formations doctorales
    private String formation;
    private String heuresRestantes;
}