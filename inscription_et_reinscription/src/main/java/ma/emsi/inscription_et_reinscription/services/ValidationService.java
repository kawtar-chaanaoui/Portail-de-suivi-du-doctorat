package ma.emsi.inscription_et_reinscription.services;

import ma.emsi.inscription_et_reinscription.dtos.ValidationDTO;
import ma.emsi.inscription_et_reinscription.entities.*;
import ma.emsi.inscription_et_reinscription.repositories.DoctorantRepository;
import ma.emsi.inscription_et_reinscription.repositories.ValidationEtapeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ValidationService {

    @Autowired
    private ValidationEtapeRepository validationRepository;

    @Autowired
    private DoctorantRepository doctorantRepository;


    private DoctorantService doctorantService;

    @Autowired
    public void setDoctorantService(@org.springframework.context.annotation.Lazy DoctorantService doctorantService) {
        this.doctorantService = doctorantService;
    }
    public void demarrerProcessusValidation(Long doctorantId) {
        Doctorant doctorant = doctorantRepository.findById(doctorantId)
                .orElseThrow(() -> new RuntimeException("Doctorant non trouvé"));

        // Étape 1: Validation par le directeur
        ValidationEtape validationDirecteur = new ValidationEtape(doctorant, TypeValidation.DIRECTEUR, 1);
        validationRepository.save(validationDirecteur);

        // Mettre à jour le statut du doctorant
        doctorant.setStatut(StatutInscription.EN_ATTENTE_DIRECTEUR);
        doctorantRepository.save(doctorant);
    }

    public void validerEtapeDirecteur(Long doctorantId, Long validateurId, String validateurNom, String commentaire) {
        Optional<ValidationEtape> etapeOpt = validationRepository
                .findByDoctorantIdAndTypeAndStatut(doctorantId, TypeValidation.DIRECTEUR, StatutValidation.EN_ATTENTE);

        if (etapeOpt.isPresent()) {
            ValidationEtape etape = etapeOpt.get();
            etape.setStatut(StatutValidation.APPROUVE);
            etape.setValidateurId(validateurId);
            etape.setValidateurNom(validateurNom);
            etape.setCommentaire(commentaire);
            etape.setDateValidation(LocalDateTime.now());
            validationRepository.save(etape);

            // Passer à l'étape suivante : validation administrative
            ValidationEtape validationAdmin = new ValidationEtape(etape.getDoctorant(), TypeValidation.ADMINISTRATIF, 2);
            validationRepository.save(validationAdmin);

            // Mettre à jour le statut du doctorant
            doctorantService.updateStatut(doctorantId, StatutInscription.EN_ATTENTE_ADMIN);
        }
    }

    public void validerEtapeAdministrative(Long doctorantId, Long validateurId, String validateurNom, String commentaire) {
        Optional<ValidationEtape> etapeOpt = validationRepository
                .findByDoctorantIdAndTypeAndStatut(doctorantId, TypeValidation.ADMINISTRATIF, StatutValidation.EN_ATTENTE);

        if (etapeOpt.isPresent()) {
            ValidationEtape etape = etapeOpt.get();
            etape.setStatut(StatutValidation.APPROUVE);
            etape.setValidateurId(validateurId);
            etape.setValidateurNom(validateurNom);
            etape.setCommentaire(commentaire);
            etape.setDateValidation(LocalDateTime.now());
            validationRepository.save(etape);

            // Finaliser l'inscription
            doctorantService.updateStatut(doctorantId, StatutInscription.VALIDE);
        }
    }

    public void rejeterEtape(Long doctorantId, TypeValidation type, Long validateurId, String validateurNom, String commentaire) {
        Optional<ValidationEtape> etapeOpt = validationRepository
                .findByDoctorantIdAndTypeAndStatut(doctorantId, type, StatutValidation.EN_ATTENTE);

        if (etapeOpt.isPresent()) {
            ValidationEtape etape = etapeOpt.get();
            etape.setStatut(StatutValidation.REJETE);
            etape.setValidateurId(validateurId);
            etape.setValidateurNom(validateurNom);
            etape.setCommentaire(commentaire);
            etape.setDateValidation(LocalDateTime.now());
            validationRepository.save(etape);

            // Rejeter l'inscription
            doctorantService.updateStatut(doctorantId, StatutInscription.REJETE);
        }
    }

    public List<ValidationDTO> getValidationsByDoctorant(Long doctorantId) {
        return validationRepository.findByDoctorantId(doctorantId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ValidationDTO> getValidationsEnAttenteByValidateur(Long validateurId, TypeValidation type) {
        return validationRepository.findByValidateurIdAndStatut(validateurId, StatutValidation.EN_ATTENTE).stream()
                .filter(v -> v.getType() == type)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ValidationDTO> getCurrentValidationEtape(Long doctorantId) {
        return validationRepository.findByDoctorantId(doctorantId).stream()
                .filter(v -> v.getStatut() == StatutValidation.EN_ATTENTE)
                .findFirst()
                .map(this::convertToDTO);
    }

    public List<ValidationDTO> getValidationsEnAttenteDirecteur(Long validateurId) {
        return getValidationsEnAttenteByValidateur(validateurId, TypeValidation.DIRECTEUR);
    }

    public List<ValidationDTO> getValidationsEnAttenteAdministratif(Long validateurId) {
        return getValidationsEnAttenteByValidateur(validateurId, TypeValidation.ADMINISTRATIF);
    }

    public boolean isEtapeDirecteurValidee(Long doctorantId) {
        return validationRepository.findByDoctorantIdAndTypeAndStatut(
                doctorantId, TypeValidation.DIRECTEUR, StatutValidation.APPROUVE).isPresent();
    }

    public boolean isEtapeAdministrativeValidee(Long doctorantId) {
        return validationRepository.findByDoctorantIdAndTypeAndStatut(
                doctorantId, TypeValidation.ADMINISTRATIF, StatutValidation.APPROUVE).isPresent();
    }

    public boolean isInscriptionValidee(Long doctorantId) {
        return isEtapeDirecteurValidee(doctorantId) && isEtapeAdministrativeValidee(doctorantId);
    }

    private ValidationDTO convertToDTO(ValidationEtape validation) {
        ValidationDTO dto = new ValidationDTO();
        dto.setId(validation.getId());
        dto.setDoctorantId(validation.getDoctorant().getId());
        dto.setDoctorantNom(validation.getDoctorant().getNom() + " " + validation.getDoctorant().getPrenom());
        dto.setType(validation.getType());
        dto.setStatut(validation.getStatut());
        dto.setCommentaire(validation.getCommentaire());
        dto.setValidateurId(validation.getValidateurId());
        dto.setValidateurNom(validation.getValidateurNom());
        dto.setDateValidation(validation.getDateValidation());
        return dto;
    }
}