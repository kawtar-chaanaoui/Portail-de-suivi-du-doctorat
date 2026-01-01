package ma.emsi.soutenance.service;


import ma.emsi.soutenance.model.Prerequis;
import ma.emsi.soutenance.repository.PrerequisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrerequisService {

    private final PrerequisRepository prerequisRepository;

    @Transactional
    public Prerequis mettreAJourPrerequis(Long prerequisId, boolean verifie,
                                          String valeurActuelle, String commentaire) {
        Prerequis prerequis = prerequisRepository.findById(prerequisId)
                .orElseThrow(() -> new RuntimeException("Prérequis non trouvé"));

        prerequis.setVerifie(verifie);
        prerequis.setValeurActuelle(valeurActuelle);
        prerequis.setCommentaire(commentaire);
        prerequis.setDateVerification(LocalDateTime.now());

        return prerequisRepository.save(prerequis);
    }

    public List<Prerequis> getPrerequisBySoutenance(Long soutenanceId) {
        return prerequisRepository.findBySoutenanceId(soutenanceId);
    }

    public boolean tousPrerequisValides(Long soutenanceId) {
        List<Prerequis> prerequis = prerequisRepository.findBySoutenanceId(soutenanceId);
        return prerequis.stream().allMatch(Prerequis::isVerifie);
    }
}