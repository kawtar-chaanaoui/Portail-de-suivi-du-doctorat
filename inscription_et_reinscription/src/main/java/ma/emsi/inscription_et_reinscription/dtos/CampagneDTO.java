package ma.emsi.inscription_et_reinscription.dtos;

import java.time.LocalDate;

public class CampagneDTO {
    private Long id;
    private String type;
    private LocalDate dateOuverture;
    private LocalDate dateFermeture;
    private boolean active;
    private String anneeUniversitaire;

    // Constructeurs
    public CampagneDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDate getDateOuverture() { return dateOuverture; }
    public void setDateOuverture(LocalDate dateOuverture) { this.dateOuverture = dateOuverture; }

    public LocalDate getDateFermeture() { return dateFermeture; }
    public void setDateFermeture(LocalDate dateFermeture) { this.dateFermeture = dateFermeture; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getAnneeUniversitaire() { return anneeUniversitaire; }
    public void setAnneeUniversitaire(String anneeUniversitaire) { this.anneeUniversitaire = anneeUniversitaire; }
}