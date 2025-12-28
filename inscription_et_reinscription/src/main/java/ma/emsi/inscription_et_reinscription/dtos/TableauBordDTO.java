package ma.emsi.inscription_et_reinscription.dtos;

import ma.emsi.inscription_et_reinscription.entities.StatutInscription;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour le tableau de bord du doctorant
 * Fournit une vue complète de l'état de l'inscription/réinscription
 */
public class TableauBordDTO {
    private Long doctorantId;
    private String nom;
    private String prenom;
    private String email;
    private StatutInscription statut;
    private boolean estReinscription;
    private Integer anneeInscriptionInitiale;
    private Integer dureeActuelle; // Nombre d'années depuis inscription initiale
    private LocalDateTime dateSoumission;
    
    // Étapes de validation
    private List<ValidationDTO> etapesValidation;
    
    // Documents uploadés
    private List<DocumentDTO> documents;
    
    // Alertes actives
    private List<AlerteDTO> alertes;
    
    // Dérogations
    private List<DerogationDTO> derogations;
    
    // Informations de progression
    private String etapeActuelle;
    private int pourcentageProgression;

    // Constructeurs
    public TableauBordDTO() {}

    // Getters and Setters
    public Long getDoctorantId() {
        return doctorantId;
    }

    public void setDoctorantId(Long doctorantId) {
        this.doctorantId = doctorantId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StatutInscription getStatut() {
        return statut;
    }

    public void setStatut(StatutInscription statut) {
        this.statut = statut;
    }

    public boolean isEstReinscription() {
        return estReinscription;
    }

    public void setEstReinscription(boolean estReinscription) {
        this.estReinscription = estReinscription;
    }

    public Integer getAnneeInscriptionInitiale() {
        return anneeInscriptionInitiale;
    }

    public void setAnneeInscriptionInitiale(Integer anneeInscriptionInitiale) {
        this.anneeInscriptionInitiale = anneeInscriptionInitiale;
    }

    public Integer getDureeActuelle() {
        return dureeActuelle;
    }

    public void setDureeActuelle(Integer dureeActuelle) {
        this.dureeActuelle = dureeActuelle;
    }

    public LocalDateTime getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(LocalDateTime dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public List<ValidationDTO> getEtapesValidation() {
        return etapesValidation;
    }

    public void setEtapesValidation(List<ValidationDTO> etapesValidation) {
        this.etapesValidation = etapesValidation;
    }

    public List<DocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentDTO> documents) {
        this.documents = documents;
    }

    public List<AlerteDTO> getAlertes() {
        return alertes;
    }

    public void setAlertes(List<AlerteDTO> alertes) {
        this.alertes = alertes;
    }

    public List<DerogationDTO> getDerogations() {
        return derogations;
    }

    public void setDerogations(List<DerogationDTO> derogations) {
        this.derogations = derogations;
    }

    public String getEtapeActuelle() {
        return etapeActuelle;
    }

    public void setEtapeActuelle(String etapeActuelle) {
        this.etapeActuelle = etapeActuelle;
    }

    public int getPourcentageProgression() {
        return pourcentageProgression;
    }

    public void setPourcentageProgression(int pourcentageProgression) {
        this.pourcentageProgression = pourcentageProgression;
    }
}
