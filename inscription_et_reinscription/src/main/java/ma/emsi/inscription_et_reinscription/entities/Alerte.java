package ma.emsi.inscription_et_reinscription.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "alerte")
public class Alerte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctorant_id", nullable = false)
    private Doctorant doctorant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeAlerte type;

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(nullable = false)
    private boolean traitee = false;

    private LocalDateTime dateTraitement;

    @Column(length = 500)
    private String message;

    // Constructeurs
    public Alerte() {}

    public Alerte(Doctorant doctorant, TypeAlerte type, String message) {
        this.doctorant = doctorant;
        this.type = type;
        this.message = message;
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

    public TypeAlerte getType() {
        return type;
    }

    public void setType(TypeAlerte type) {
        this.type = type;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isTraitee() {
        return traitee;
    }

    public void setTraitee(boolean traitee) {
        this.traitee = traitee;
    }

    public LocalDateTime getDateTraitement() {
        return dateTraitement;
    }

    public void setDateTraitement(LocalDateTime dateTraitement) {
        this.dateTraitement = dateTraitement;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
