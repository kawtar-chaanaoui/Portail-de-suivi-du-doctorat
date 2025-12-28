package ma.emsi.inscription_et_reinscription.services;

import ma.emsi.inscription_et_reinscription.dtos.AlerteDTO;
import ma.emsi.inscription_et_reinscription.entities.Alerte;
import ma.emsi.inscription_et_reinscription.entities.Doctorant;
import ma.emsi.inscription_et_reinscription.entities.TypeAlerte;
import ma.emsi.inscription_et_reinscription.repositories.AlerteRepository;
import ma.emsi.inscription_et_reinscription.repositories.DoctorantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlerteService {

    @Autowired
    private AlerteRepository alerteRepository;

    @Autowired
    private DoctorantRepository doctorantRepository;

    /**
     * Créer une alerte manuelle
     */
    public AlerteDTO creerAlerte(Long doctorantId, TypeAlerte type, String message) {
        Doctorant doctorant = doctorantRepository.findById(doctorantId)
                .orElseThrow(() -> new RuntimeException("Doctorant non trouvé"));

        Alerte alerte = new Alerte(doctorant, type, message);
        Alerte saved = alerteRepository.save(alerte);

        return convertToDTO(saved);
    }

    /**
     * Récupérer les alertes actives d'un doctorant
     */
    public List<AlerteDTO> getAlertesActives(Long doctorantId) {
        Doctorant doctorant = doctorantRepository.findById(doctorantId)
                .orElseThrow(() -> new RuntimeException("Doctorant non trouvé"));

        return alerteRepository.findByDoctorantAndTraiteeFalse(doctorant).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer toutes les alertes d'un doctorant
     */
    public List<AlerteDTO> getAllAlertes(Long doctorantId) {
        Doctorant doctorant = doctorantRepository.findById(doctorantId)
                .orElseThrow(() -> new RuntimeException("Doctorant non trouvé"));

        return alerteRepository.findByDoctorant(doctorant).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Marquer une alerte comme traitée
     */
    public void marquerAlerteTraitee(Long alerteId) {
        Alerte alerte = alerteRepository.findById(alerteId)
                .orElseThrow(() -> new RuntimeException("Alerte non trouvée"));

        alerte.setTraitee(true);
        alerte.setDateTraitement(LocalDateTime.now());
        alerteRepository.save(alerte);
    }

    /**
     * Tâche planifiée pour vérifier les durées et générer des alertes
     * S'exécute tous les jours à 1h du matin
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void verifierEtGenererAlertes() {
        List<Doctorant> doctorants = doctorantRepository.findAll();
        int anneeActuelle = Year.now().getValue();

        for (Doctorant doctorant : doctorants) {
            if (doctorant.getAnneeInscriptionInitiale() == null) {
                continue;
            }

            int duree = anneeActuelle - doctorant.getAnneeInscriptionInitiale();

            // Alerte à 2.5 ans (30 mois) - Approche de la limite de 3 ans
            if (duree >= 2 && duree < 3) {
                creerAlerteAutomatique(doctorant, TypeAlerte.LIMITE_3_ANS_APPROCHEE,
                        "Attention : Vous approchez de la limite de 3 ans. " +
                        "Préparez votre demande de dérogation si nécessaire.");
            }

            // Alerte à 3 ans - Limite de réinscription atteinte
            if (duree >= 3 && duree < 6) {
                creerAlerteAutomatique(doctorant, TypeAlerte.LIMITE_3_ANS_ATTEINTE,
                        "IMPORTANT : Limite de 3 ans atteinte. " +
                        "Une dérogation du PED est obligatoire pour poursuivre.");
            }

            // Alerte à 5 ans - Approche de la limite maximale
            if (duree >= 5 && duree < 6) {
                creerAlerteAutomatique(doctorant, TypeAlerte.LIMITE_6_ANS_APPROCHEE,
                        "ALERTE CRITIQUE : Vous approchez de la limite maximale de 6 ans. " +
                        "Préparez votre soutenance rapidement.");
            }

            // Alerte à 6 ans - Limite maximale atteinte
            if (duree >= 6) {
                creerAlerteAutomatique(doctorant, TypeAlerte.LIMITE_6_ANS_ATTEINTE,
                        "LIMITE MAXIMALE ATTEINTE : 6 ans de doctorat. " +
                        "Vous ne pouvez plus vous réinscrire.");
            }
        }
    }

    /**
     * Créer une alerte automatique (évite les doublons)
     */
    private void creerAlerteAutomatique(Doctorant doctorant, TypeAlerte type, String message) {
        // Vérifier si une alerte de ce type existe déjà (non traitée)
        List<Alerte> alertesExistantes = alerteRepository
                .findByDoctorantAndTraiteeFalse(doctorant).stream()
                .filter(a -> a.getType() == type)
                .collect(Collectors.toList());

        if (alertesExistantes.isEmpty()) {
            Alerte alerte = new Alerte(doctorant, type, message);
            alerteRepository.save(alerte);
        }
    }

    private AlerteDTO convertToDTO(Alerte alerte) {
        AlerteDTO dto = new AlerteDTO();
        dto.setId(alerte.getId());
        dto.setDoctorantId(alerte.getDoctorant().getId());
        dto.setDoctorantNom(alerte.getDoctorant().getNom() + " " + alerte.getDoctorant().getPrenom());
        dto.setType(alerte.getType());
        dto.setMessage(alerte.getMessage());
        dto.setDateCreation(alerte.getDateCreation());
        dto.setTraitee(alerte.isTraitee());
        return dto;
    }
}
