package ma.emsi.inscription_et_reinscription.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

import java.time.LocalDate;


@Entity
@Table(name = "inscription_campagne")
public class InscriptionCampagne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // "INSCRIPTION" ou "REINSCRIPTION"

    @Column(nullable = false)
    private LocalDate dateOuverture;

    @Column(nullable = false)
    private LocalDate dateFermeture;

    private boolean active = true;

    private String anneeUniversitaire;

    // Constructeurs
    public InscriptionCampagne() {}

    public InscriptionCampagne(String type, LocalDate dateOuverture, LocalDate dateFermeture, String anneeUniversitaire) {
        this.type = type;
        this.dateOuverture = dateOuverture;
        this.dateFermeture = dateFermeture;
        this.anneeUniversitaire = anneeUniversitaire;
    }

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