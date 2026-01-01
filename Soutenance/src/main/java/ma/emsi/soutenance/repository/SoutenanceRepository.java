package ma.emsi.soutenance.repository;


import ma.emsi.soutenance.model.Soutenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SoutenanceRepository extends JpaRepository<Soutenance, Long> {
    List<Soutenance> findByDoctorantId(Long doctorantId);
    List<Soutenance> findByDirecteurId(Long directeurId);
    List<Soutenance> findByStatut(Soutenance.StatutSoutenance statut);
    Optional<Soutenance> findByIdAndDoctorantId(Long id, Long doctorantId);
}
