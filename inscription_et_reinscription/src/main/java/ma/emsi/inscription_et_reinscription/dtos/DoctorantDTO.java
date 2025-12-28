package ma.emsi.inscription_et_reinscription.dtos;



import ma.emsi.inscription_et_reinscription.entities.StatutInscription;
import java.time.LocalDateTime;

public class DoctorantDTO {
    private Long id;
    private String cin;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String titreThese;
    private String laboratoire;
    private StatutInscription statut;
    private LocalDateTime dateSoumission;
    private LocalDateTime dateValidation;
    private String directeurNom;
    private boolean estReinscription;

    // Constructeurs
    public DoctorantDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getTitreThese() { return titreThese; }
    public void setTitreThese(String titreThese) { this.titreThese = titreThese; }

    public String getLaboratoire() { return laboratoire; }
    public void setLaboratoire(String laboratoire) { this.laboratoire = laboratoire; }

    public StatutInscription getStatut() { return statut; }
    public void setStatut(StatutInscription statut) { this.statut = statut; }

    public LocalDateTime getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(LocalDateTime dateSoumission) { this.dateSoumission = dateSoumission; }

    public LocalDateTime getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDateTime dateValidation) { this.dateValidation = dateValidation; }

    public String getDirecteurNom() { return directeurNom; }
    public void setDirecteurNom(String directeurNom) { this.directeurNom = directeurNom; }

    public boolean isEstReinscription() { return estReinscription; }
    public void setEstReinscription(boolean estReinscription) { this.estReinscription = estReinscription; }
}