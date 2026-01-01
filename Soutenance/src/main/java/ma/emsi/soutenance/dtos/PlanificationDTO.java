package ma.emsi.soutenance.dtos;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PlanificationDTO {
    private Long soutenanceId;
    private LocalDateTime dateSoutenance;
    private String lieu;
    private String salle;
}