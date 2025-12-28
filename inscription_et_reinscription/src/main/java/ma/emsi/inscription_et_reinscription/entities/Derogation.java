package ma.emsi.inscription_et_reinscription.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "derogation")
public class Derogation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctorant_id", nullable = false)
    private Doctorant doctorant;

    @Column(nullable = false, length = 1000)
    private String motif;

    @Column(nullable = false)
    private LocalDateTime dateDemande = LocalDateTime.now();

    private LocalDateTime dateDecision;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDerogation statut = StatutDerogation.EN_ATTENTE;

    // Référence au validateur PED (Pôle d'Études Doctorale)
    private Long validateurPedId;
    private String validateurPedNom;

    @Column(length = 1000)
    private String commentairePed;

    // Constructeurs
    public Derogation() {}

    public Derogation(Doctorant doctorant, String motif) {
        this.doctorant = doctorant;
        this.motif = motif;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctorant getDoctorant() {
        return doctorant;
    }

    public void setDoctorant(Doctorant doctorant) {
        this.doctorant = doctorant;
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

    public Long getValidateurPedId() {
        return validateurPedId;
    }

    public void setValidateurPedId(Long validateurPedId) {
        this.validateurPedId = validateurPedId;
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
