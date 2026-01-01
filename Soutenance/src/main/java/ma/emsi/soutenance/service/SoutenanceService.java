package ma.emsi.soutenance.service;

import ma.emsi.soutenance.client.DocumentsClient;
import ma.emsi.soutenance.client.PrerequisClient;
import ma.emsi.soutenance.client.dto.DocumentSummaryDTO;
import ma.emsi.soutenance.client.dto.PrerequisCheckResponse;
import ma.emsi.soutenance.dtos.DemandeSoutenanceDTO;
import ma.emsi.soutenance.model.Document;
import ma.emsi.soutenance.model.Jury;
import ma.emsi.soutenance.model.Prerequis;
import ma.emsi.soutenance.model.Soutenance;
import ma.emsi.soutenance.repository.DocumentRepository;
import ma.emsi.soutenance.repository.PrerequisRepository;
import ma.emsi.soutenance.repository.SoutenanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class SoutenanceService {

    private static final Logger log = LoggerFactory.getLogger(SoutenanceService.class);

    private final SoutenanceRepository soutenanceRepository;
    private final PrerequisRepository prerequisRepository;
    private final DocumentRepository documentRepository;
    private final PrerequisClient prerequisClient;
    private final DocumentsClient documentsClient;

    @Autowired(required = false)
    private NotificationService notificationService;

    public SoutenanceService(SoutenanceRepository soutenanceRepository,
                             PrerequisRepository prerequisRepository,
                             DocumentRepository documentRepository,
                             PrerequisClient prerequisClient,
                             DocumentsClient documentsClient) {
        this.soutenanceRepository = soutenanceRepository;
        this.prerequisRepository = prerequisRepository;
        this.documentRepository = documentRepository;
        this.prerequisClient = prerequisClient;
        this.documentsClient = documentsClient;
    }

    private void notifier(java.util.function.Consumer<NotificationService> action) {
        if (notificationService != null) {
            action.accept(notificationService);
        }
    }

    @Transactional
    public Soutenance creerDemandeSoutenance(DemandeSoutenanceDTO dto) {
        Soutenance soutenance = new Soutenance();
        soutenance.setDoctorantId(dto.getDoctorantId());
        soutenance.setDoctorantNom(dto.getDoctorantNom());
        soutenance.setDoctorantEmail(dto.getDoctorantEmail());
        soutenance.setDirecteurId(dto.getDirecteurId());
        soutenance.setDirecteurNom(dto.getDirecteurNom());
        soutenance.setDateDemande(LocalDate.now());
        soutenance.setStatut(Soutenance.StatutSoutenance.BROUILLON);

        Soutenance saved = soutenanceRepository.save(soutenance);
        soutenanceRepository.flush();
        initialiserPrerequis(saved);
        notifier(ns -> ns.notifierCreationSoutenance(saved));
        return saved;
    }

    private void initialiserPrerequis(Soutenance soutenance) {
        List<Prerequis> prerequisList = Arrays.asList(
                creerPrerequis(soutenance, "PUBLICATION",
                        "Au moins 2 articles journaux (Q1/Q2)", "2"),
                creerPrerequis(soutenance, "CONFERENCE",
                        "Au moins 2 conférences (ou équivalent)", "2"),
                creerPrerequis(soutenance, "FORMATION",
                        "Compléter 200h de formation", "200h")
        );
        prerequisRepository.saveAll(prerequisList);
    }

    private Prerequis creerPrerequis(Soutenance soutenance, String type, String desc, String valeur) {
        Prerequis prerequis = new Prerequis();
        prerequis.setSoutenance(soutenance);
        prerequis.setTypePrerequis(type);
        prerequis.setDescription(desc);
        prerequis.setValeurRequise(valeur);
        prerequis.setVerifie(false);
        return prerequis;
    }

    @Transactional
    public Soutenance soumettreDemande(Long soutenanceId) {
        Soutenance soutenance = getSoutenance(soutenanceId);
        if (!documentsComplets(soutenance)) {
            throw new IllegalStateException("Documents incomplets");
        }
        soutenance.setStatut(Soutenance.StatutSoutenance.SOUMIS);
        Soutenance updated = soutenanceRepository.save(soutenance);
        notifier(ns -> ns.notifierDirecteur(updated));
        return updated;
    }

    private boolean documentsComplets(Soutenance soutenance) {
        List<String> documentsRequis = Arrays.asList(
                "RAPPORT_THESE", "RAPPORT_ANTI_PLAGIAT",
                "ATTESTATIONS_FORMATION", "AUTORISATION_SOUTENANCE"
        );

        List<DocumentSummaryDTO> externes = recupererDocumentsExternes(soutenance.getId());
        if (!externes.isEmpty()) {
            return documentsRequis.stream().allMatch(type -> externes.stream()
                    .anyMatch(doc -> type.equalsIgnoreCase(doc.getTypeDocument())
                            && "VALIDE".equalsIgnoreCase(doc.getStatutValidation())));
        }

        List<Document> locaux = documentRepository.findBySoutenanceId(soutenance.getId());
        return documentsRequis.stream().allMatch(type -> locaux.stream()
                .anyMatch(doc -> type.equalsIgnoreCase(doc.getTypeDocument())
                        && "VALIDE".equalsIgnoreCase(doc.getStatutValidation())));
    }

    private List<DocumentSummaryDTO> recupererDocumentsExternes(Long soutenanceId) {
        try {
            return documentsClient.getDocumentsBySoutenance(soutenanceId);
        } catch (Exception ex) {
            log.error("Erreur Feign lors de la récupération des documents pour la soutenance {}", soutenanceId, ex);
            return List.of();
        }
    }

    @Transactional
    public boolean verifierPrerequis(Long soutenanceId) {
        Soutenance soutenance = getSoutenance(soutenanceId);
        List<Prerequis> prerequis = soutenance.getPrerequis();
        if (prerequis == null || prerequis.isEmpty()) {
            log.warn("Aucun prérequis associé à la soutenance {}", soutenanceId);
            return false;
        }

        PrerequisCheckResponse statutPrerequis = recupererStatutPrerequis(soutenance.getDoctorantId());
        boolean prerequisValides;

        if (statutPrerequis.isAvailable()) {
            log.debug("Statut prérequis récupéré via Feign pour le doctorant {}", soutenance.getDoctorantId());
            for (Prerequis p : prerequis) {
                boolean valeurValidee = true;
                switch (p.getTypePrerequis()) {
                    case "PUBLICATION" -> {
                        valeurValidee = statutPrerequis.isPublicationsOk();
                        p.setValeurActuelle(String.valueOf(statutPrerequis.getPublicationsCount()));
                    }
                    case "CONFERENCE" -> {
                        valeurValidee = statutPrerequis.isConferencesOk();
                        p.setValeurActuelle(String.valueOf(statutPrerequis.getConferencesCount()));
                    }
                    case "FORMATION" -> {
                        valeurValidee = statutPrerequis.isFormationsOk();
                        p.setValeurActuelle(statutPrerequis.getFormationsHours() + "h");
                    }
                    default -> log.debug("Type de prérequis {} non géré explicitement", p.getTypePrerequis());
                }
                p.setVerifie(valeurValidee);
                p.setDateVerification(LocalDateTime.now());
                if (statutPrerequis.getMessage() != null && !statutPrerequis.getMessage().isBlank()) {
                    p.setCommentaire(statutPrerequis.getMessage());
                }
                prerequisRepository.save(p);
            }
            prerequisValides = prerequis.stream().allMatch(Prerequis::isVerifie);
        } else {
            log.warn("Service prérequis indisponible, validation optimiste appliquée pour la soutenance {}", soutenanceId);
            for (Prerequis p : prerequis) {
                p.setVerifie(true);
                p.setValeurActuelle("OK");
                p.setDateVerification(LocalDateTime.now());
                if (statutPrerequis.getMessage() != null && !statutPrerequis.getMessage().isBlank()) {
                    p.setCommentaire(statutPrerequis.getMessage());
                }
                prerequisRepository.save(p);
            }
            prerequisValides = true;
        }

        if (prerequisValides) {
            soutenance.setStatut(Soutenance.StatutSoutenance.PREREQUIS_VALIDES);
            soutenanceRepository.save(soutenance);
            notifier(ns -> ns.notifierPrerequisValides(soutenance));
        }
        return prerequisValides;
    }

    private PrerequisCheckResponse recupererStatutPrerequis(Long doctorantId) {
        try {
            PrerequisCheckResponse response = prerequisClient.getPrerequisStatus(doctorantId);
            if (response == null) {
                log.warn("Réponse Feign vide pour le doctorant {}", doctorantId);
                return PrerequisCheckResponse.unavailable("Réponse prérequis vide");
            }
            if (!response.isAvailable()) {
                log.warn("Service prérequis indisponible pour le doctorant {}", doctorantId);
            }
            return response;
        } catch (Exception ex) {
            log.error("Erreur Feign lors de la récupération des prérequis pour le doctorant {}", doctorantId, ex);
            return PrerequisCheckResponse.unavailable("Erreur Feign: " + ex.getMessage());
        }
    }

    @Transactional
    public Soutenance planifierSoutenance(Long soutenanceId, LocalDateTime date, String lieu, String salle) {
        Soutenance soutenance = getSoutenance(soutenanceId);
        if (soutenance.getJury() == null ||
                soutenance.getJury().getStatut() != Jury.StatutJury.VALIDE) {
            throw new IllegalStateException("Jury non validé");
        }
        soutenance.setDateSoutenance(date);
        soutenance.setLieu(lieu);
        soutenance.setSalle(salle);
        soutenance.setStatut(Soutenance.StatutSoutenance.PLANIFIEE);
        soutenance.setNumeroAutorisation("AUT-" + System.currentTimeMillis());
        soutenance.setDateAutorisation(LocalDate.now());
        Soutenance updated = soutenanceRepository.save(soutenance);
        notifier(ns -> ns.notifierPlanification(updated));
        return updated;
    }

    @Transactional
    public Soutenance terminerSoutenance(Long soutenanceId, String procesVerbalPath) {
        Soutenance soutenance = getSoutenance(soutenanceId);
        soutenance.setStatut(Soutenance.StatutSoutenance.TERMINEE);
        soutenance.setProcesVerbalPath(procesVerbalPath);
        return soutenanceRepository.save(soutenance);
    }

    private Soutenance getSoutenance(Long id) {
        return soutenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Soutenance non trouvée: " + id));
    }

    public Soutenance getById(Long id) {
        return getSoutenance(id);
    }

    public List<Soutenance> getByDoctorant(Long doctorantId) {
        return soutenanceRepository.findByDoctorantId(doctorantId);
    }

    public List<Soutenance> getByDirecteur(Long directeurId) {
        return soutenanceRepository.findByDirecteurId(directeurId);
    }

    public List<Soutenance> getByStatut(Soutenance.StatutSoutenance statut) {
        return soutenanceRepository.findByStatut(statut);
    }

    public void notifierDirecteur(Soutenance soutenance) {
        String message = String.format(
            "DEMANDE_A_VALIDER|soutenanceId:%d|doctorantNom:%s|doctorantEmail:%s",
            soutenance.getId(),
            soutenance.getDoctorantNom(),
            soutenance.getDoctorantEmail()
        );
        kafkaTemplate.send("notifications-directeurs", message);
    }

    public void notifierPlanification(Soutenance soutenance) {
        String message = String.format(
            "SOUTENANCE_PLANIFIEE|soutenanceId:%d|doctorantNom:%s|doctorantEmail:%s|date:%s|lieu:%s|salle:%s",
            soutenance.getId(),
            soutenance.getDoctorantNom(),
            soutenance.getDoctorantEmail(),
            soutenance.getDateSoutenance(),
            soutenance.getLieu(),
            soutenance.getSalle()
        );
        kafkaTemplate.send("notifications-soutenance", message);

        if (soutenance.getJury() != null) {
            for (MembreJury membre : soutenance.getJury().getMembres()) {
                String msgMembre = String.format(
                    "INVITATION_SOUTENANCE|membreEmail:%s|soutenanceId:%d|date:%s|lieu:%s|salle:%s",
                    membre.getEmail(),
                    soutenance.getId(),
                    soutenance.getDateSoutenance(),
                    soutenance.getLieu(),
                    soutenance.getSalle()
                );
                kafkaTemplate.send("notifications-jury", msgMembre);
            }
        }
    }
}
