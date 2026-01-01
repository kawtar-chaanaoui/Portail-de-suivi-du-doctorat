package ma.emsi.soutenance.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "documents_soutenance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "soutenance_id", nullable = false)
    private Soutenance soutenance;

    @Column(name = "type_document", nullable = false)
    private String typeDocument;

    @Column(name = "nom_fichier", nullable = false)
    private String nomFichier;

    @Column(name = "chemin_fichier", nullable = false)
    private String cheminFichier;

    @Column(name = "chemin_stockage", nullable = false)
    private String cheminStockage;

    @Column(name = "taille_octets", nullable = false)
    private Long tailleOctets;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "date_depot", nullable = false)
    private LocalDateTime dateDepot = LocalDateTime.now();

    @Column(name = "depot_par", nullable = false)
    private String depotPar;

    @Column(name = "statut_validation")
    private String statutValidation = "EN_ATTENTE";

    @Column(name = "commentaire_validation")
    private String commentaireValidation;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
        this.cheminStockage = cheminFichier;
    }
}