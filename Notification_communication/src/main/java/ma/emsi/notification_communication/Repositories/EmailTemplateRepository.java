package ma.emsi.notification_communication.Repositories;

import ma.emsi.notification_communication.entite.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    Optional<EmailTemplate> findByCodeAndActiveTrue(String code);
    Optional<EmailTemplate> findByCode(String code);
    Optional<EmailTemplate> findByName(String name);
}
