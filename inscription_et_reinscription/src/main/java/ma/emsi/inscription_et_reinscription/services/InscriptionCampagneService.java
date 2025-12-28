package ma.emsi.inscription_et_reinscription.services;



import ma.emsi.inscription_et_reinscription.dtos.CampagneDTO;
import ma.emsi.inscription_et_reinscription.entities.InscriptionCampagne;
import ma.emsi.inscription_et_reinscription.repositories.InscriptionCampagneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InscriptionCampagneService {

    @Autowired
    private InscriptionCampagneRepository campagneRepository;

    public List<CampagneDTO> getAllCampagnes() {
        return campagneRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CampagneDTO> getCampagnesActives() {
        return campagneRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CampagneDTO> getCampagneActiveByType(String type) {
        return campagneRepository.findByTypeAndActiveTrue(type)
                .map(this::convertToDTO);
    }

    public CampagneDTO createCampagne(CampagneDTO campagneDTO) {
        InscriptionCampagne campagne = new InscriptionCampagne();
        campagne.setType(campagneDTO.getType());
        campagne.setDateOuverture(campagneDTO.getDateOuverture());
        campagne.setDateFermeture(campagneDTO.getDateFermeture());
        campagne.setAnneeUniversitaire(campagneDTO.getAnneeUniversitaire());
        campagne.setActive(true);

        InscriptionCampagne saved = campagneRepository.save(campagne);
        return convertToDTO(saved);
    }

    public CampagneDTO updateCampagne(Long id, CampagneDTO campagneDTO) {
        Optional<InscriptionCampagne> existing = campagneRepository.findById(id);
        if (existing.isPresent()) {
            InscriptionCampagne campagne = existing.get();
            campagne.setDateOuverture(campagneDTO.getDateOuverture());
            campagne.setDateFermeture(campagneDTO.getDateFermeture());
            campagne.setAnneeUniversitaire(campagneDTO.getAnneeUniversitaire());
            campagne.setActive(campagneDTO.isActive());

            InscriptionCampagne updated = campagneRepository.save(campagne);
            return convertToDTO(updated);
        }
        return null;
    }

    public void desactiverCampagne(Long id) {
        campagneRepository.findById(id).ifPresent(campagne -> {
            campagne.setActive(false);
            campagneRepository.save(campagne);
        });
    }

    public boolean isCampagneActive(String type) {
        Optional<InscriptionCampagne> campagne = campagneRepository.findByTypeAndActiveTrue(type);
        return campagne.isPresent() &&
                LocalDate.now().isAfter(campagne.get().getDateOuverture()) &&
                LocalDate.now().isBefore(campagne.get().getDateFermeture());
    }

    private CampagneDTO convertToDTO(InscriptionCampagne campagne) {
        CampagneDTO dto = new CampagneDTO();
        dto.setId(campagne.getId());
        dto.setType(campagne.getType());
        dto.setDateOuverture(campagne.getDateOuverture());
        dto.setDateFermeture(campagne.getDateFermeture());
        dto.setActive(campagne.isActive());
        dto.setAnneeUniversitaire(campagne.getAnneeUniversitaire());
        return dto;
    }
}