package ma.emsi.soutenance.repository;

import ma.emsi.soutenance.model.MembreJury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembreJuryRepository extends JpaRepository<MembreJury, Long> {
    List<MembreJury> findByJuryId(Long juryId);
    List<MembreJury> findByRole(MembreJury.RoleJury role);
    List<MembreJury> findByJuryIdAndRole(Long juryId, MembreJury.RoleJury role);
    List<MembreJury> findByJuryIdAndConfirme(Long juryId, boolean confirme);
}

