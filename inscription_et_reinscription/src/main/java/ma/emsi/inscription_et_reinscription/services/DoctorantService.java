package ma.emsi.inscription_et_reinscription.services;

import ma.emsi.inscription_et_reinscription.dtos.DoctorantDTO;
import ma.emsi.inscription_et_reinscription.dtos.InscriptionRequestDTO;
import ma.emsi.inscription_et_reinscription.entities.StatutInscription;

import java.util.List;
import java.util.Optional;

public interface DoctorantService {

    DoctorantDTO soumettreInscription(InscriptionRequestDTO request);

    DoctorantDTO demanderReinscription(Long doctorantId);

    List<DoctorantDTO> getDoctorantsByStatut(StatutInscription statut);

    Optional<DoctorantDTO> getDoctorantById(Long id);

    Optional<DoctorantDTO> getDoctorantByUserId(Long userId);

    Optional<DoctorantDTO> getDoctorantByCin(String cin);

    List<DoctorantDTO> getDoctorantsByDirecteur(Long directeurUserId);

    void updateStatut(Long doctorantId, StatutInscription statut);
}