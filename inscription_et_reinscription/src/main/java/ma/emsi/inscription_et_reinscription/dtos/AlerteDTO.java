package ma.emsi.inscription_et_reinscription.dtos;

import ma.emsi.inscription_et_reinscription.entities.TypeAlerte;
import java.time.LocalDateTime;

public class AlerteDTO {
    private Long id;
    private Long doctorantId;
    private String doctorantNom;
    private TypeAlerte type;
    private String message;
    private LocalDateTime dateCreation;
    private boolean traitee;

    // Constructeurs
    public AlerteDTO() {}

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

    public TypeAlerte getType() {
        return type;
    }

    public void setType(TypeAlerte type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
