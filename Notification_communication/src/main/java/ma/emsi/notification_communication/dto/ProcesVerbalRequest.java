package ma.emsi.notification_communication.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProcesVerbalRequest {
    private String studentName;
    private String thesisTitle;
    private LocalDateTime soutenanceDateTime;
    private String location;
    private String decision;
    private String mention;
    private List<JuryMember> juryMembers;
    private List<String> observations;

    @Data
    public static class JuryMember {
        private String name;
        private String role;  // Président, Rapporteur, Examinateur, Directeur
        private String institution;
    }
}