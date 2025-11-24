package ma.emsi.inscription_et_reinscription.repositories;

import ma.emsi.inscription_et_reinscription.entities.StatutValidation;
import ma.emsi.inscription_et_reinscription.entities.TypeValidation;
import ma.emsi.inscription_et_reinscription.entities.ValidationEtape;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ValidationEtapeRepository extends JpaRepository<ValidationEtape, Long> {
    List<ValidationEtape> findByDoctorantId(Long doctorantId);
    List<ValidationEtape> findByDoctorantIdAndType(Long doctorantId, TypeValidation type);
    Optional<ValidationEtape> findByDoctorantIdAndTypeAndStatut(Long doctorantId, TypeValidation type, StatutValidation statut);
    List<ValidationEtape> findByValidateurIdAndStatut(Long validateurId, StatutValidation statut);
}