package ma.emsi.soutenance.repository;

import ma.emsi.soutenance.model.Prerequis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PrerequisRepository extends JpaRepository<Prerequis, Long> {
    List<Prerequis> findBySoutenanceId(Long soutenanceId);
    List<Prerequis> findBySoutenanceIdAndVerifie(Long soutenanceId, boolean verifie);
}