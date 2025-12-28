package ma.emsi.inscription_et_reinscription.repositories;

import ma.emsi.inscription_et_reinscription.entities.Document;
import ma.emsi.inscription_et_reinscription.entities.TypeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByDoctorantId(Long doctorantId);
    List<Document> findByDoctorantIdAndTypeDocument(Long doctorantId, TypeDocument typeDocument);
    void deleteByDoctorantId(Long doctorantId);
}