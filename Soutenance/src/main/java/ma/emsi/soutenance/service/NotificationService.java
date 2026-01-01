package ma.emsi.soutenance.service;


import ma.emsi.soutenance.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void notifierCreationSoutenance(Soutenance soutenance) {
        String message = String.format(
                "NOUVELLE_DEMANDE|doctorantId:%d|doctorantEmail:%s|doctorantNom:%s|soutenanceId:%d|statut:%s",
                soutenance.getDoctorantId(),
                soutenance.getDoctorantEmail(),
                soutenance.getDoctorantNom(),
                soutenance.getId(),
                soutenance.getStatut()
        );
        kafkaTemplate.send("notifications-soutenance", message);
    }

    public void notifierDirecteur(Soutenance soutenance) {
        String message = String.format(
                "DEMANDE_A_VALIDER|directeur:%d|soutenance:%d|doctorant:%s",
                soutenance.getDirecteurId(),
                soutenance.getId(),
                soutenance.getDoctorantNom()
        );
        kafkaTemplate.send("notifications-directeurs", message);
    }

    public void notifierPropositionJury(Jury jury) {
        String message = String.format(
                "JURY_PROPOSE|soutenance:%d|directeur:%s|membres:%d",
                jury.getSoutenance().getId(),
                jury.getDirecteurNom(),
                jury.getMembres().size()
        );
        kafkaTemplate.send("notifications-admin", message);
    }

    public void notifierPrerequisValides(Soutenance soutenance) {
        String message = String.format(
                "PREREQUIS_VALIDES|soutenance:%d|doctorant:%s",
                soutenance.getId(),
                soutenance.getDoctorantNom()
        );
        kafkaTemplate.send("notifications-admin", message);
    }

    public void notifierJuryValide(Jury jury) {
        String message = String.format(
                "JURY_VALIDE|soutenance:%d|doctorant:%s",
                jury.getSoutenance().getId(),
                jury.getSoutenance().getDoctorantNom()
        );
        kafkaTemplate.send("notifications-soutenance", message);
    }

    public void notifierPlanification(Soutenance soutenance) {
        String message = String.format(
                "SOUTENANCE_PLANIFIEE|soutenance:%d|date:%s|lieu:%s",
                soutenance.getId(),
                soutenance.getDateSoutenance(),
                soutenance.getLieu()
        );
        kafkaTemplate.send("notifications-soutenance", message);

        // Notifier chaque membre du jury
        if (soutenance.getJury() != null) {
            for (MembreJury membre : soutenance.getJury().getMembres()) {
                String msgMembre = String.format(
                        "INVITATION_SOUTENANCE|membre:%s|soutenance:%d|date:%s|lieu:%s",
                        membre.getEmail(),
                        soutenance.getId(),
                        soutenance.getDateSoutenance(),
                        soutenance.getLieu()
                );
                kafkaTemplate.send("notifications-jury", msgMembre);
            }
        }
    }
}