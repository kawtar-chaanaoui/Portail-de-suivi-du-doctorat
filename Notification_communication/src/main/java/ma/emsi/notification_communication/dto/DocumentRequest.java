package ma.emsi.notification_communication.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DocumentRequest {
    // Champs communs
    private String studentName;
    private String studentId;
    
    // Pour l'attestation d'inscription
    private String academicYear;
    
    // Pour l'autorisation de soutenance
    private String thesisTitle;
    private LocalDateTime soutenanceDateTime;
    
    // Pour le procès-verbal
    private List<JuryMember> juryMembers;
    private String decision;
    private String mention;
    private List<String> observations;
    
    @Data
    public static class JuryMember {
        private String name;
        private String role;  // Président, Rapporteur, Examinateur, Directeur
        private String institution;
    }
}