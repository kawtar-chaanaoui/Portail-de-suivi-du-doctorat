package ma.emsi.inscription_et_reinscription.repositories;

import ma.emsi.inscription_et_reinscription.entities.Derogation;
import ma.emsi.inscription_et_reinscription.entities.Doctorant;
import ma.emsi.inscription_et_reinscription.entities.StatutDerogation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DerogationRepository extends JpaRepository<Derogation, Long> {
    List<Derogation> findByDoctorant(Doctorant doctorant);
    List<Derogation> findByStatut(StatutDerogation statut);
    Optional<Derogation> findByDoctorantAndStatut(Doctorant doctorant, StatutDerogation statut);
}
