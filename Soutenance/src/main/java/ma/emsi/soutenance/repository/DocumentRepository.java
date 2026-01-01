package ma.emsi.soutenance.repository;


import ma.emsi.soutenance.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findBySoutenanceId(Long soutenanceId);
    List<Document> findBySoutenanceIdAndTypeDocument(Long soutenanceId, String typeDocument);
}