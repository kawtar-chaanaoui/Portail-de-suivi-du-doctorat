package ma.emsi.inscription_et_reinscription.repositories;

import ma.emsi.inscription_et_reinscription.entities.Alerte;
import ma.emsi.inscription_et_reinscription.entities.Doctorant;
import ma.emsi.inscription_et_reinscription.entities.TypeAlerte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlerteRepository extends JpaRepository<Alerte, Long> {
    List<Alerte> findByDoctorantAndTraiteeFalse(Doctorant doctorant);
    List<Alerte> findByDoctorant(Doctorant doctorant);
    List<Alerte> findByTraiteeFalse();
    List<Alerte> findByTypeAndTraiteeFalse(TypeAlerte type);
}
