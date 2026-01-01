package ma.emsi.soutenance.dtos;

import lombok.Data;
import java.util.List;

@Data
public class PropositionJuryDTO {
    private Long soutenanceId;
    private Long directeurTheseId;
    private String directeurNom;
    private List<MembreJuryDTO> membres;
}

