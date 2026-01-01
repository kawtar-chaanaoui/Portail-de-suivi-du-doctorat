package ma.emsi.soutenance.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "membres_jury")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembreJury {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation MANY-TO-ONE avec Jury
    @ManyToOne
    @JoinColumn(name = "jury_id", nullable = false)
    private Jury jury;

    @Column(name = "nom_complet", nullable = false)
    private String nomComplet;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "institution", nullable = false)
    private String institution;

    @Column(name = "grade")
    private String grade; // Professeur, Maître de conférences, etc.

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleJury role;

    @Column(name = "a_confirme")
    private boolean confirme = false;

    @Column(name = "date_confirmation")
    private LocalDateTime dateConfirmation;

    @Column(name = "rapport_depose")
    private boolean rapportDepose = false;

    public enum RoleJury {
        RAPPORTEUR,
        PRESIDENT,
        EXAMINATEUR,
        INVITE
    }
}
