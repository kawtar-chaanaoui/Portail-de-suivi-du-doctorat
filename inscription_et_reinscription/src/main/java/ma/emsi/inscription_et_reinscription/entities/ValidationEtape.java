package ma.emsi.inscription_et_reinscription.entities;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "validation_etape")
public class ValidationEtape {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctorant_id", nullable = false)
    private Doctorant doctorant;

    @Enumerated(EnumType.STRING)
    private TypeValidation type;

    @Enumerated(EnumType.STRING)
    private StatutValidation statut = StatutValidation.EN_ATTENTE;

    private LocalDateTime dateValidation;
    private String commentaire;

    // Référence à l'utilisateur validateur (externe)
    private Long validateurId;
    private String validateurNom;

    private Integer ordre; // Ordre dans le processus (1: directeur, 2: admin)

    // Constructeurs
    public ValidationEtape() {}

    public ValidationEtape(Doctorant doctorant, TypeValidation type, Integer ordre) {
        this.doctorant = doctorant;
        this.type = type;
        this.ordre = ordre;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Doctorant getDoctorant() { return doctorant; }
    public void setDoctorant(Doctorant doctorant) { this.doctorant = doctorant; }

    public TypeValidation getType() { return type; }
    public void setType(TypeValidation type) { this.type = type; }

    public StatutValidation getStatut() { return statut; }
    public void setStatut(StatutValidation statut) { this.statut = statut; }

    public LocalDateTime getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDateTime dateValidation) { this.dateValidation = dateValidation; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public Long getValidateurId() { return validateurId; }
    public void setValidateurId(Long validateurId) { this.validateurId = validateurId; }

    public String getValidateurNom() { return validateurNom; }
    public void setValidateurNom(String validateurNom) { this.validateurNom = validateurNom; }

    public Integer getOrdre() { return ordre; }
    public void setOrdre(Integer ordre) { this.ordre = ordre; }
}