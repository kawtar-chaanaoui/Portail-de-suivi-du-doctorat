package ma.emsi.inscription_et_reinscription.services;

import ma.emsi.inscription_et_reinscription.dtos.DerogationDTO;
import ma.emsi.inscription_et_reinscription.entities.Derogation;
import ma.emsi.inscription_et_reinscription.entities.Doctorant;
import ma.emsi.inscription_et_reinscription.entities.StatutDerogation;
import ma.emsi.inscription_et_reinscription.repositories.DerogationRepository;
import ma.emsi.inscription_et_reinscription.repositories.DoctorantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DerogationService {

    @Autowired
    private DerogationRepository derogationRepository;

    @Autowired
    private DoctorantRepository doctorantRepository;

    /**
     * Demander une dérogation pour un doctorant ayant dépassé 3 ans
     */
    public DerogationDTO demanderDerogation(Long doctorantId, String motif) {
        Doctorant doctorant = doctorantRepository.findById(doctorantId)
                .orElseThrow(() -> new RuntimeException("Doctorant non trouvé"));

        // Vérifier qu'il n'y a pas déjà une dérogation en attente
        Optional<Derogation> derogationExistante = derogationRepository
                .findByDoctorantAndStatut(doctorant, StatutDerogation.EN_ATTENTE);
        
        if (derogationExistante.isPresent()) {
            throw new RuntimeException("Une demande de dérogation est déjà en cours pour ce doctorant");
        }

        Derogation derogation = new Derogation(doctorant, motif);
        Derogation saved = derogationRepository.save(derogation);

        return convertToDTO(saved);
    }

    /**
     * Valider une dérogation par le PED
     */
    public DerogationDTO validerDerogation(Long derogationId, Long validateurPedId, 
                                          String validateurPedNom, String commentaire) {
        Derogation derogation = derogationRepository.findById(derogationId)
                .orElseThrow(() -> new RuntimeException("Dérogation non trouvée"));

        if (derogation.getStatut() != StatutDerogation.EN_ATTENTE) {
            throw new RuntimeException("Cette dérogation a déjà été traitée");
        }

        derogation.setStatut(StatutDerogation.ACCORDEE);
        derogation.setValidateurPedId(validateurPedId);
        derogation.setValidateurPedNom(validateurPedNom);
        derogation.setCommentairePed(commentaire);
        derogation.setDateDecision(LocalDateTime.now());

        Derogation updated = derogationRepository.save(derogation);
        return convertToDTO(updated);
    }

    /**
     * Refuser une dérogation
     */
    public DerogationDTO rejeterDerogation(Long derogationId, Long validateurPedId, 
                                          String validateurPedNom, String commentaire) {
        Derogation derogation = derogationRepository.findById(derogationId)
                .orElseThrow(() -> new RuntimeException("Dérogation non trouvée"));

        if (derogation.getStatut() != StatutDerogation.EN_ATTENTE) {
            throw new RuntimeException("Cette dérogation a déjà été traitée");
        }

        derogation.setStatut(StatutDerogation.REFUSEE);
        derogation.setValidateurPedId(validateurPedId);
        derogation.setValidateurPedNom(validateurPedNom);
        derogation.setCommentairePed(commentaire);
        derogation.setDateDecision(LocalDateTime.now());

        Derogation updated = derogationRepository.save(derogation);
        return convertToDTO(updated);
    }

    /**
     * Récupérer toutes les dérogations d'un doctorant
     */
    public List<DerogationDTO> getDerogationsByDoctorant(Long doctorantId) {
        Doctorant doctorant = doctorantRepository.findById(doctorantId)
                .orElseThrow(() -> new RuntimeException("Doctorant non trouvé"));

        return derogationRepository.findByDoctorant(doctorant).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer toutes les dérogations en attente
     */
    public List<DerogationDTO> getDerogationsEnAttente() {
        return derogationRepository.findByStatut(StatutDerogation.EN_ATTENTE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Vérifier si un doctorant a une dérogation accordée
     */
    public boolean hasDerogationAccordee(Long doctorantId) {
        Doctorant doctorant = doctorantRepository.findById(doctorantId).orElse(null);
        if (doctorant == null) {
            return false;
        }

        return derogationRepository.findByDoctorantAndStatut(doctorant, StatutDerogation.ACCORDEE)
                .isPresent();
    }

    private DerogationDTO convertToDTO(Derogation derogation) {
        DerogationDTO dto = new DerogationDTO();
        dto.setId(derogation.getId());
        dto.setDoctorantId(derogation.getDoctorant().getId());
        dto.setDoctorantNom(derogation.getDoctorant().getNom());
        dto.setDoctorantPrenom(derogation.getDoctorant().getPrenom());
        dto.setMotif(derogation.getMotif());
        dto.setDateDemande(derogation.getDateDemande());
        dto.setDateDecision(derogation.getDateDecision());
        dto.setStatut(derogation.getStatut());
        dto.setValidateurPedNom(derogation.getValidateurPedNom());
        dto.setCommentairePed(derogation.getCommentairePed());
        return dto;
    }
}
