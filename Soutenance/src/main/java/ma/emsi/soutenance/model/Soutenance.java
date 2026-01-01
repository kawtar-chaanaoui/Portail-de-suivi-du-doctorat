package ma.emsi.soutenance.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "soutenances")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Soutenance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long doctorantId;

    private String doctorantNom;
    private String doctorantEmail;
    private Long directeurId;
    private String directeurNom;

    @CreatedDate
    @Column(name = "date_demande", nullable = false, updatable = false)
    private LocalDate dateDemande;

    @Enumerated(EnumType.STRING)
    private StatutSoutenance statut;

    @OneToMany(mappedBy = "soutenance", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Document> documents;

    @OneToMany(mappedBy = "soutenance", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prerequis> prerequis;

    @OneToOne(cascade = CascadeType.ALL)
    private Jury jury;

    private LocalDateTime dateSoutenance;
    private String lieu;
    private String salle;
    private String numeroAutorisation;
    private LocalDate dateAutorisation;
    private String procesVerbalPath;

    public enum StatutSoutenance {
        BROUILLON, SOUMIS, EN_ATTENTE_DIRECTEUR,
        EN_ATTENTE_JURY, PREREQUIS_VALIDES,
        JURY_VALIDE, PLANIFIEE, TERMINEE
    }
}
