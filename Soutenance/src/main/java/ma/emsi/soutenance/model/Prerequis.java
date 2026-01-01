package ma.emsi.soutenance.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Prerequis {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "soutenance_id")
    private Soutenance soutenance;

    private String typePrerequis; // PUBLICATION, FORMATION, CONFERENCE
    private String description;
    private boolean verifie;
    private String valeurRequise; // "2", "200h", "2"
    private String valeurActuelle; // "3", "210h", "3"
    private String commentaire;
    private LocalDateTime dateVerification;
}
