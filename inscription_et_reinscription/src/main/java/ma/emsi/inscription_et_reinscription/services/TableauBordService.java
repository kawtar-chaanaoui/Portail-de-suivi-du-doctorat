package ma.emsi.inscription_et_reinscription.services;

import ma.emsi.inscription_et_reinscription.dtos.*;
import ma.emsi.inscription_et_reinscription.entities.Doctorant;
import ma.emsi.inscription_et_reinscription.entities.StatutInscription;
import ma.emsi.inscription_et_reinscription.repositories.DoctorantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

@Service
@Transactional
public class TableauBordService {

    @Autowired
    private DoctorantRepository doctorantRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AlerteService alerteService;

    @Autowired
    private DerogationService derogationService;

    /**
     * Obtenir le tableau de bord complet d'un doctorant
     */
    public TableauBordDTO getTableauBord(Long doctorantId) {
        Doctorant doctorant = doctorantRepository.findById(doctorantId)
                .orElseThrow(() -> new RuntimeException("Doctorant non trouvé"));

        TableauBordDTO tableauBord = new TableauBordDTO();
        
        // Informations de base
        tableauBord.setDoctorantId(doctorant.getId());
        tableauBord.setNom(doctorant.getNom());
        tableauBord.setPrenom(doctorant.getPrenom());
        tableauBord.setEmail(doctorant.getEmail());
        tableauBord.setStatut(doctorant.getStatut());
        tableauBord.setEstReinscription(doctorant.isEstReinscription());
        tableauBord.setAnneeInscriptionInitiale(doctorant.getAnneeInscriptionInitiale());
        tableauBord.setDateSoumission(doctorant.getDateSoumission());

        // Calculer la durée actuelle
        if (doctorant.getAnneeInscriptionInitiale() != null) {
            int duree = Year.now().getValue() - doctorant.getAnneeInscriptionInitiale();
            tableauBord.setDureeActuelle(duree);
        }

        // Étapes de validation
        List<ValidationDTO> validations = validationService.getValidationsByDoctorant(doctorantId);
        tableauBord.setEtapesValidation(validations);

        // Documents
        List<DocumentDTO> documents = documentService.getDocumentsByDoctorant(doctorantId);
        tableauBord.setDocuments(documents);

        // Alertes actives
        List<AlerteDTO> alertes = alerteService.getAlertesActives(doctorantId);
        tableauBord.setAlertes(alertes);

        // Dérogations
        List<DerogationDTO> derogations = derogationService.getDerogationsByDoctorant(doctorantId);
        tableauBord.setDerogations(derogations);

        // Déterminer l'étape actuelle et le pourcentage de progression
        determinerProgression(tableauBord, doctorant.getStatut());

        return tableauBord;
    }

    /**
     * Déterminer l'étape actuelle et le pourcentage de progression
     */
    private void determinerProgression(TableauBordDTO tableauBord, StatutInscription statut) {
        switch (statut) {
            case BROUILLON:
                tableauBord.setEtapeActuelle("Brouillon - En cours de rédaction");
                tableauBord.setPourcentageProgression(10);
                break;
            case SOUMIS:
                tableauBord.setEtapeActuelle("Dossier soumis - En attente de traitement");
                tableauBord.setPourcentageProgression(25);
                break;
            case EN_ATTENTE_DIRECTEUR:
                tableauBord.setEtapeActuelle("En attente de validation du directeur de thèse");
                tableauBord.setPourcentageProgression(40);
                break;
            case EN_ATTENTE_ADMIN:
                tableauBord.setEtapeActuelle("En attente de validation administrative");
                tableauBord.setPourcentageProgression(70);
                break;
            case VALIDE:
                tableauBord.setEtapeActuelle("Inscription validée ✓");
                tableauBord.setPourcentageProgression(100);
                break;
            case REJETE:
                tableauBord.setEtapeActuelle("Dossier rejeté");
                tableauBord.setPourcentageProgression(0);
                break;
            default:
                tableauBord.setEtapeActuelle("Statut inconnu");
                tableauBord.setPourcentageProgression(0);
        }
    }
}
