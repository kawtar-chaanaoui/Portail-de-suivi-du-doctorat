package ma.emsi.soutenance.repository;

import ma.emsi.soutenance.model.Jury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JuryRepository extends JpaRepository<Jury, Long> {
    Optional<Jury> findBySoutenanceId(Long soutenanceId);
    List<Jury> findByDirecteurTheseId(Long directeurId);
    List<Jury> findByStatut(Jury.StatutJury statut);
}
