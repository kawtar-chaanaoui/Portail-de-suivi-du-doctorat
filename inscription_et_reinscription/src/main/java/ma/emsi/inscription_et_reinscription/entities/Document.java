package ma.emsi.inscription_et_reinscription.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomFichier;

    @Enumerated(EnumType.STRING)
    private TypeDocument typeDocument;

    @Column(nullable = false)
    private String cheminFichier;

    private Long tailleFichier;

    @Column(nullable = false)
    private LocalDateTime dateDepot = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "doctorant_id", nullable = false)
    private Doctorant doctorant;

    // Constructeurs
    public Document() {}

    public Document(String nomFichier, TypeDocument typeDocument, String cheminFichier, Doctorant doctorant) {
        this.nomFichier = nomFichier;
        this.typeDocument = typeDocument;
        this.cheminFichier = cheminFichier;
        this.doctorant = doctorant;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomFichier() { return nomFichier; }
    public void setNomFichier(String nomFichier) { this.nomFichier = nomFichier; }

    public TypeDocument getTypeDocument() { return typeDocument; }
    public void setTypeDocument(TypeDocument typeDocument) { this.typeDocument = typeDocument; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }

    public Long getTailleFichier() { return tailleFichier; }
    public void setTailleFichier(Long tailleFichier) { this.tailleFichier = tailleFichier; }

    public LocalDateTime getDateDepot() { return dateDepot; }
    public void setDateDepot(LocalDateTime dateDepot) { this.dateDepot = dateDepot; }

    public Doctorant getDoctorant() { return doctorant; }
    public void setDoctorant(Doctorant doctorant) { this.doctorant = doctorant; }
}