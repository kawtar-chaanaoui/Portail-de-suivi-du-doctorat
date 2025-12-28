package ma.emsi.inscription_et_reinscription.repositories;


import ma.emsi.inscription_et_reinscription.entities.InscriptionCampagne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionCampagneRepository extends JpaRepository<InscriptionCampagne, Long> {
    Optional<InscriptionCampagne> findByTypeAndActiveTrue(String type);
    List<InscriptionCampagne> findByActiveTrue();
    List<InscriptionCampagne> findByDateFermetureAfter(LocalDate date);
    Optional<InscriptionCampagne> findByAnneeUniversitaireAndType(String anneeUniversitaire, String type);
}