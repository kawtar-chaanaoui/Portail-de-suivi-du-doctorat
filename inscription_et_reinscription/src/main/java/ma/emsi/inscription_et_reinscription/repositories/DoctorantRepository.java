package ma.emsi.inscription_et_reinscription.repositories;

import ma.emsi.inscription_et_reinscription.entities.Doctorant;
import ma.emsi.inscription_et_reinscription.entities.StatutInscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorantRepository extends JpaRepository<Doctorant, Long> {
    Optional<Doctorant> findByCin(String cin);
    Optional<Doctorant> findByEmail(String email);
    List<Doctorant> findByStatut(StatutInscription statut);
    List<Doctorant> findByDirecteurId(Long directeurId);
    List<Doctorant> findByAnneeInscriptionInitiale(Integer annee);

    @Query("SELECT d FROM Doctorant d WHERE d.estReinscription = false AND d.anneeInscriptionInitiale <= :anneeLimite")
    List<Doctorant> findDoctorantsAyantDepasseDuree(Integer anneeLimite);

    @Query("SELECT COUNT(d) FROM Doctorant d WHERE d.campagne.id = :campagneId AND d.statut = :statut")
    Long countByCampagneAndStatut(Long campagneId, StatutInscription statut);
}