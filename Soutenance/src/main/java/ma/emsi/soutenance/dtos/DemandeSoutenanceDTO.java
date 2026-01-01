package ma.emsi.soutenance.dtos;


import lombok.Data;

@Data
public class DemandeSoutenanceDTO {
    private Long doctorantId;
    private String doctorantNom;
    private String doctorantEmail;
    private Long directeurId;
    private String directeurNom;
}