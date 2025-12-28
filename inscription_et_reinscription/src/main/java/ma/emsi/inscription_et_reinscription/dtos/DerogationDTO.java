package ma.emsi.inscription_et_reinscription.dtos;

import ma.emsi.inscription_et_reinscription.entities.StatutDerogation;
import java.time.LocalDateTime;

public class DerogationDTO {
    private Long id;
    private Long doctorantId;
    private String doctorantNom;
    private String doctorantPrenom;
    private String motif;
    private LocalDateTime dateDemande;
    private LocalDateTime dateDecision;
    private StatutDerogation statut;
    private String validateurPedNom;
    private String commentairePed;

    // Constructeurs
    public DerogationDTO() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorantId() {
        return doctorantId;
    }

    public void setDoctorantId(Long doctorantId) {
        this.doctorantId = doctorantId;
    }

    public String getDoctorantNom() {
        return doctorantNom;
    }

    public void setDoctorantNom(String doctorantNom) {
        this.doctorantNom = doctorantNom;
    }

    public String getDoctorantPrenom() {
        return doctorantPrenom;
    }

    public void setDoctorantPrenom(String doctorantPrenom) {
        this.doctorantPrenom = doctorantPrenom;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public LocalDateTime getDateDecision() {
        return dateDecision;
    }

    public void setDateDecision(LocalDateTime dateDecision) {
        this.dateDecision = dateDecision;
    }

    public StatutDerogation getStatut() {
        return statut;
    }

    public void setStatut(StatutDerogation statut) {
        this.statut = statut;
    }

    public String getValidateurPedNom() {
        return validateurPedNom;
    }

    public void setValidateurPedNom(String validateurPedNom) {
        this.validateurPedNom = validateurPedNom;
    }

    public String getCommentairePed() {
        return commentairePed;
    }

    public void setCommentairePed(String commentairePed) {
        this.commentairePed = commentairePed;
    }
}
