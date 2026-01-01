package ma.emsi.soutenance.dtos;

import lombok.Data;

@Data
public class MembreJuryDTO {
    private String nomComplet;
    private String email;
    private String institution;
    private String grade;
    private String role;
}

