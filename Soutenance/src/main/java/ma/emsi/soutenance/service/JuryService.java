package ma.emsi.soutenance.service;

import ma.emsi.soutenance.model.*;
import ma.emsi.soutenance.repository.*;
import ma.emsi.soutenance.dtos.PropositionJuryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JuryService {

    private final JuryRepository juryRepository;
    private final MembreJuryRepository membreJuryRepository;
    private final SoutenanceRepository soutenanceRepository;

    @Autowired(required = false)
    private NotificationService notificationService;

    public JuryService(JuryRepository juryRepository,
                      MembreJuryRepository membreJuryRepository,
                      SoutenanceRepository soutenanceRepository) {
        this.juryRepository = juryRepository;
        this.membreJuryRepository = membreJuryRepository;
        this.soutenanceRepository = soutenanceRepository;
    }

    private void notifier(java.util.function.Consumer<NotificationService> action) {
        if (notificationService != null) {
            action.accept(notificationService);
        }
    }

    // CdC: Proposition du jury par le Directeur de Thèse
    @Transactional
    public Jury proposerJury(PropositionJuryDTO dto) {
        Soutenance soutenance = soutenanceRepository.findById(dto.getSoutenanceId())
                .orElseThrow(() -> new RuntimeException("Soutenance non trouvée"));

        // Vérifier que le directeur est bien le directeur de la thèse
        if (!soutenance.getDirecteurId().equals(dto.getDirecteurTheseId())) {
            throw new IllegalStateException("Seul le directeur de thèse peut proposer le jury");
        }

        // Créer le jury
        Jury jury = new Jury();
        jury.setSoutenance(soutenance);
        jury.setDirecteurTheseId(dto.getDirecteurTheseId());
        jury.setDirecteurNom(dto.getDirecteurNom());
        jury.setDateProposition(LocalDateTime.now());
        jury.setStatut(Jury.StatutJury.PROPOSE);

        // Créer les membres
        List<MembreJury> membres = dto.getMembres().stream()
                .map(mDto -> {
                    MembreJury membre = new MembreJury();
                    membre.setJury(jury);
                    membre.setNomComplet(mDto.getNomComplet());
                    membre.setEmail(mDto.getEmail());
                    membre.setInstitution(mDto.getInstitution());
                    membre.setGrade(mDto.getGrade());
                    membre.setRole(MembreJury.RoleJury.valueOf(mDto.getRole()));
                    return membre;
                })
                .collect(Collectors.toList());

        // Vérifier qu'il y a au moins 2 rapporteurs
        long nbRapporteurs = membres.stream()
                .filter(m -> m.getRole() == MembreJury.RoleJury.RAPPORTEUR)
                .count();

        if (nbRapporteurs < 2) {
            throw new IllegalArgumentException("Un jury doit avoir au moins 2 rapporteurs");
        }

        jury.setMembres(membres);

        // Sauvegarder
        Jury saved = juryRepository.save(jury);

        // Mettre à jour le statut de la soutenance
        soutenance.setStatut(Soutenance.StatutSoutenance.EN_ATTENTE_JURY);
        soutenanceRepository.save(soutenance);

        // Notifier l'administration
        notifier(ns -> ns.notifierPropositionJury(saved));

        return saved;
    }

    // Valider le jury (par l'administration)
    @Transactional
    public Jury validerJury(Long juryId) {
        Jury jury = getJury(juryId);

        jury.setStatut(Jury.StatutJury.VALIDE);
        Jury updated = juryRepository.save(jury);

        // Mettre à jour le statut de la soutenance
        Soutenance soutenance = jury.getSoutenance();
        soutenance.setStatut(Soutenance.StatutSoutenance.JURY_VALIDE);
        soutenanceRepository.save(soutenance);

        // Notifier les membres du jury
        notifier(ns -> ns.notifierJuryValide(updated));

        return updated;
    }

    // Confirmer présence d'un membre
    @Transactional
    public MembreJury confirmerPresence(Long membreId) {
        MembreJury membre = getMembreJury(membreId);

        membre.setConfirme(true);
        membre.setDateConfirmation(LocalDateTime.now());

        return membreJuryRepository.save(membre);
    }

    // Helper methods
    private Jury getJury(Long id) {
        return juryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jury non trouvé: " + id));
    }

    private MembreJury getMembreJury(Long id) {
        return membreJuryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membre du jury non trouvé: " + id));
    }

    public Jury getBySoutenance(Long soutenanceId) {
        return juryRepository.findBySoutenanceId(soutenanceId)
                .orElseThrow(() -> new RuntimeException("Jury non trouvé pour soutenance: " + soutenanceId));
    }

    public List<Jury> getByDirecteur(Long directeurId) {
        return juryRepository.findByDirecteurTheseId(directeurId);
    }
}