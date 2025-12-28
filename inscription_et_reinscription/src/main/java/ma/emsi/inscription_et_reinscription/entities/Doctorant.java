package ma.emsi.inscription_et_reinscription.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctorant")
public class Doctorant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Champs de base (corrigés)
    @Column(unique = true, nullable = false)
    private String cin;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    private String telephone;

    // Informations de thèse
    private String titreThese;
    private String laboratoire;
    private String equipeRecherche;
    private String domaineRecherche;

    // Référence au directeur
    private Long directeurId;
    private String directeurNom;

    @Enumerated(EnumType.STRING)
    private StatutInscription statut = StatutInscription.BROUILLON;

    private LocalDateTime dateSoumission;
    private LocalDateTime dateValidation;

    // Pour la réinscription
    private Integer anneeInscriptionInitiale;
    private boolean estReinscription = false;

    // Campagne associée
    @ManyToOne
    @JoinColumn(name = "campagne_id")
    private InscriptionCampagne campagne;

    // Constructeurs
    public Doctorant() {}

    // Getters and Setters CORRIGÉS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getTitreThese() { return titreThese; }
    public void setTitreThese(String titreThese) { this.titreThese = titreThese; }

    public String getLaboratoire() { return laboratoire; }
    public void setLaboratoire(String laboratoire) { this.laboratoire = laboratoire; }

    public String getEquipeRecherche() { return equipeRecherche; }
    public void setEquipeRecherche(String equipeRecherche) { this.equipeRecherche = equipeRecherche; }

    public String getDomaineRecherche() { return domaineRecherche; }
    public void setDomaineRecherche(String domaineRecherche) { this.domaineRecherche = domaineRecherche; }

    public Long getDirecteurId() { return directeurId; }
    public void setDirecteurId(Long directeurId) { this.directeurId = directeurId; }

    public String getDirecteurNom() { return directeurNom; }
    public void setDirecteurNom(String directeurNom) { this.directeurNom = directeurNom; }

    public StatutInscription getStatut() { return statut; }
    public void setStatut(StatutInscription statut) { this.statut = statut; }

    public LocalDateTime getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(LocalDateTime dateSoumission) { this.dateSoumission = dateSoumission; }

    public LocalDateTime getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDateTime dateValidation) { this.dateValidation = dateValidation; }

    public Integer getAnneeInscriptionInitiale() { return anneeInscriptionInitiale; }
    public void setAnneeInscriptionInitiale(Integer anneeInscriptionInitiale) { this.anneeInscriptionInitiale = anneeInscriptionInitiale; }

    public boolean isEstReinscription() { return estReinscription; }
    public void setEstReinscription(boolean estReinscription) { this.estReinscription = estReinscription; }

    public InscriptionCampagne getCampagne() { return campagne; }
    public void setCampagne(InscriptionCampagne campagne) { this.campagne = campagne; }
}