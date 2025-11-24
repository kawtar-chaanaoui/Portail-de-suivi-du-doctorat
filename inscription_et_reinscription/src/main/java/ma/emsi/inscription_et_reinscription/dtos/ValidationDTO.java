package ma.emsi.inscription_et_reinscription.dtos;


import ma.emsi.inscription_et_reinscription.entities.StatutValidation;
import ma.emsi.inscription_et_reinscription.entities.TypeValidation;

import java.time.LocalDateTime;

public class ValidationDTO {
    private Long id;
    private Long doctorantId;
    private String doctorantNom;
    private TypeValidation type;
    private StatutValidation statut;
    private String commentaire;
    private Long validateurId;
    private String validateurNom;
    private LocalDateTime dateValidation;

    // Constructeurs
    public ValidationDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDoctorantId() { return doctorantId; }
    public void setDoctorantId(Long doctorantId) { this.doctorantId = doctorantId; }

    public String getDoctorantNom() { return doctorantNom; }
    public void setDoctorantNom(String doctorantNom) { this.doctorantNom = doctorantNom; }

    public TypeValidation getType() { return type; }
    public void setType(TypeValidation type) { this.type = type; }

    public StatutValidation getStatut() { return statut; }
    public void setStatut(StatutValidation statut) { this.statut = statut; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public Long getValidateurId() { return validateurId; }
    public void setValidateurId(Long validateurId) { this.validateurId = validateurId; }

    public String getValidateurNom() { return validateurNom; }
    public void setValidateurNom(String validateurNom) { this.validateurNom = validateurNom; }

    public LocalDateTime getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDateTime dateValidation) { this.dateValidation = dateValidation; }
}