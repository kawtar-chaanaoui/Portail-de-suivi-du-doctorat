package ma.emsi.soutenance.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "jurys")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jury {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation ONE-TO-ONE avec Soutenance (CdC: chaque soutenance a un jury)
    @OneToOne
    @JoinColumn(name = "soutenance_id")
    private Soutenance soutenance;

    // Directeur qui propose le jury (CdC page 3)
    @Column(name = "directeur_these_id", nullable = false)
    private Long directeurTheseId;

    @Column(name = "directeur_nom")
    private String directeurNom;

    // Liste des membres du jury
    @OneToMany(mappedBy = "jury", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MembreJury> membres;

    @Column(name = "date_proposition")
    private LocalDateTime dateProposition;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutJury statut;

    public enum StatutJury {
        PROPOSE,
        VALIDE,
        CONFIRME,
        ANNULE
    }
}