package ma.emsi.inscription_et_reinscription.services;

import ma.emsi.inscription_et_reinscription.dtos.DoctorantDTO;
import ma.emsi.inscription_et_reinscription.dtos.InscriptionRequestDTO;
import ma.emsi.inscription_et_reinscription.entities.*;
import ma.emsi.inscription_et_reinscription.repositories.DoctorantRepository;
import ma.emsi.inscription_et_reinscription.repositories.InscriptionCampagneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DoctorantServiceImpl implements DoctorantService {

    @Autowired
    private DoctorantRepository doctorantRepository;

    @Autowired
    private InscriptionCampagneRepository campagneRepository;

    @Autowired
    private DocumentService documentService;


    private ValidationService validationService;

    @Autowired
    public void setValidationService(@org.springframework.context.annotation.Lazy ValidationService validationService) {
        this.validationService = validationService;
    }

    @Override
    public DoctorantDTO soumettreInscription(InscriptionRequestDTO request) {
        // Vérifier campagne active
        String typeCampagne = request.isEstReinscription() ? "REINSCRIPTION" : "INSCRIPTION";
        InscriptionCampagne campagne = campagneRepository.findByTypeAndActiveTrue(typeCampagne)
                .orElseThrow(() -> new RuntimeException("Aucune campagne " + typeCampagne.toLowerCase() + " active"));

        if (!isCampagneOuverte(campagne)) {
            throw new RuntimeException("La campagne " + typeCampagne.toLowerCase() + " est fermée");
        }

        // Vérifier unicité CIN
        if (doctorantRepository.findByCin(request.getCin()).isPresent()) {
            throw new RuntimeException("Un doctorant avec ce CIN existe déjà");
        }

        // Vérifier unicité email
        if (doctorantRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Un doctorant avec cet email existe déjà");
        }

        // Pour les réinscriptions, vérifier la durée maximale
        if (request.isEstReinscription()) {
            Optional<Doctorant> inscriptionInitiale = doctorantRepository.findByCin(request.getCin());
            if (inscriptionInitiale.isPresent()) {
                int duree = Year.now().getValue() - inscriptionInitiale.get().getAnneeInscriptionInitiale();
                if (duree > 3) {
                    throw new RuntimeException("Durée maximale de réinscription (3 ans) dépassée. Dérogation nécessaire.");
                }
                if (duree >= 6) {
                    throw new RuntimeException("Durée maximale du doctorat (6 ans) atteinte");
                }
            }
        }

        // Créer doctorant
        Doctorant doctorant = new Doctorant();

        // Données personnelles (CORRIGÉ)
        doctorant.setCin(request.getCin());
        doctorant.setNom(request.getNom());
        doctorant.setPrenom(request.getPrenom());
        doctorant.setEmail(request.getEmail());
        doctorant.setTelephone(request.getTelephone());

        // Informations spécifiques au doctorat
        doctorant.setTitreThese(request.getTitreThese());
        doctorant.setLaboratoire(request.getLaboratoire());
        doctorant.setEquipeRecherche(request.getEquipeRecherche());
        doctorant.setDomaineRecherche(request.getDomaineRecherche());
        doctorant.setDirecteurId(request.getDirecteurUserId());
        doctorant.setDirecteurNom(request.getDirecteurNom());
        doctorant.setStatut(StatutInscription.SOUMIS);
        doctorant.setDateSoumission(LocalDateTime.now());
        doctorant.setEstReinscription(request.isEstReinscription());
        doctorant.setCampagne(campagne);

        // Pour l'inscription initiale, enregistrer l'année
        if (!request.isEstReinscription()) {
            doctorant.setAnneeInscriptionInitiale(Year.now().getValue());
        }

        Doctorant saved = doctorantRepository.save(doctorant);

        // Sauvegarder les documents
        if (request.getDocuments() != null && !request.getDocuments().isEmpty()) {
            documentService.saveDocuments(saved, request.getDocuments());
        }

        // Démarrer le processus de validation
        validationService.demarrerProcessusValidation(saved.getId());

        return convertToDTO(saved);
    }

    @Override
    public DoctorantDTO demanderReinscription(Long doctorantId) {
        Doctorant existing = doctorantRepository.findById(doctorantId)
                .orElseThrow(() -> new RuntimeException("Doctorant non trouvé"));

        // Vérifier durée maximale de 6 ans
        int dureeTotale = Year.now().getValue() - existing.getAnneeInscriptionInitiale();
        if (dureeTotale >= 6) {
            throw new RuntimeException("Durée maximale du doctorat (6 ans) atteinte");
        }

        // Vérifier qu'une campagne de réinscription est active
        InscriptionCampagne campagne = campagneRepository.findByTypeAndActiveTrue("REINSCRIPTION")
                .orElseThrow(() -> new RuntimeException("Aucune campagne de réinscription active"));

        if (!isCampagneOuverte(campagne)) {
            throw new RuntimeException("La campagne de réinscription est fermée");
        }

        // Créer une nouvelle inscription pour l'année N+1
        Doctorant reinscription = new Doctorant();

        // Copier les données personnelles (CORRIGÉ)
        reinscription.setCin(existing.getCin());
        reinscription.setNom(existing.getNom());
        reinscription.setPrenom(existing.getPrenom());
        reinscription.setEmail(existing.getEmail());
        reinscription.setTelephone(existing.getTelephone());

        // Copier les informations spécifiques
        reinscription.setTitreThese(existing.getTitreThese());
        reinscription.setLaboratoire(existing.getLaboratoire());
        reinscription.setEquipeRecherche(existing.getEquipeRecherche());
        reinscription.setDomaineRecherche(existing.getDomaineRecherche());
        reinscription.setDirecteurId(existing.getDirecteurId());
        reinscription.setDirecteurNom(existing.getDirecteurNom());
        reinscription.setAnneeInscriptionInitiale(existing.getAnneeInscriptionInitiale());
        reinscription.setStatut(StatutInscription.SOUMIS);
        reinscription.setDateSoumission(LocalDateTime.now());
        reinscription.setEstReinscription(true);
        reinscription.setCampagne(campagne);

        Doctorant saved = doctorantRepository.save(reinscription);

        // Démarrer le processus de validation
        validationService.demarrerProcessusValidation(saved.getId());

        return convertToDTO(saved);
    }

    @Override
    public List<DoctorantDTO> getDoctorantsByStatut(StatutInscription statut) {
        return doctorantRepository.findByStatut(statut).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DoctorantDTO> getDoctorantById(Long id) {
        return doctorantRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public Optional<DoctorantDTO> getDoctorantByUserId(Long userId) {
        // Pour l'instant, on utilise l'ID du doctorant directement
        return doctorantRepository.findById(userId).map(this::convertToDTO);
    }

    @Override
    public Optional<DoctorantDTO> getDoctorantByCin(String cin) {
        return doctorantRepository.findByCin(cin).map(this::convertToDTO);
    }

    @Override
    public List<DoctorantDTO> getDoctorantsByDirecteur(Long directeurUserId) {
        return doctorantRepository.findByDirecteurId(directeurUserId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStatut(Long doctorantId, StatutInscription statut) {
        doctorantRepository.findById(doctorantId).ifPresent(doctorant -> {
            doctorant.setStatut(statut);
            if (statut == StatutInscription.VALIDE) {
                doctorant.setDateValidation(LocalDateTime.now());
            }
            doctorantRepository.save(doctorant);
        });
    }

    private boolean isCampagneOuverte(InscriptionCampagne campagne) {
        LocalDate now = LocalDate.now();
        return now.isAfter(campagne.getDateOuverture()) && now.isBefore(campagne.getDateFermeture());
    }

    private DoctorantDTO convertToDTO(Doctorant doctorant) {
        DoctorantDTO dto = new DoctorantDTO();
        dto.setId(doctorant.getId());
        dto.setCin(doctorant.getCin());
        dto.setNom(doctorant.getNom());
        dto.setPrenom(doctorant.getPrenom());
        dto.setEmail(doctorant.getEmail());
        dto.setTelephone(doctorant.getTelephone());
        dto.setTitreThese(doctorant.getTitreThese());
        dto.setLaboratoire(doctorant.getLaboratoire());
        dto.setStatut(doctorant.getStatut());
        dto.setDateSoumission(doctorant.getDateSoumission());
        dto.setDateValidation(doctorant.getDateValidation());
        dto.setDirecteurNom(doctorant.getDirecteurNom());
        dto.setEstReinscription(doctorant.isEstReinscription());
        return dto;
    }
}