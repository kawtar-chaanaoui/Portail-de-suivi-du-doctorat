package ma.emsi.inscription_et_reinscription.dtos;

import java.util.List;

public class InscriptionRequestDTO {
    private Long userId; // Ajouter ce champ
    private String cin;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String titreThese;
    private String laboratoire;
    private String equipeRecherche;
    private String domaineRecherche;
    private Long directeurUserId;
    private String directeurNom;
    private List<DocumentDTO> documents;
    private boolean estReinscription = false;

    // Constructeurs
    public InscriptionRequestDTO() {}

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

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

    public String getEquipeRecherche() { return equipeRecherche; }
    public void setEquipeRecherche(String equipeRecherche) { this.equipeRecherche = equipeRecherche; }

    public String getDomaineRecherche() { return domaineRecherche; }
    public void setDomaineRecherche(String domaineRecherche) { this.domaineRecherche = domaineRecherche; }

    public Long getDirecteurUserId() { return directeurUserId; }
    public void setDirecteurUserId(Long directeurUserId) { this.directeurUserId = directeurUserId; }

    public String getDirecteurNom() { return directeurNom; }
    public void setDirecteurNom(String directeurNom) { this.directeurNom = directeurNom; }

    public List<DocumentDTO> getDocuments() { return documents; }
    public void setDocuments(List<DocumentDTO> documents) { this.documents = documents; }

    public boolean isEstReinscription() { return estReinscription; }
    public void setEstReinscription(boolean estReinscription) { this.estReinscription = estReinscription; }
}