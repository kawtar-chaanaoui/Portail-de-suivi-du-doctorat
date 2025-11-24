package ma.emsi.inscription_et_reinscription.services;



import ma.emsi.inscription_et_reinscription.dtos.DocumentDTO;
import ma.emsi.inscription_et_reinscription.entities.Document;
import ma.emsi.inscription_et_reinscription.entities.Doctorant;
import ma.emsi.inscription_et_reinscription.entities.TypeDocument;
import ma.emsi.inscription_et_reinscription.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Base64;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    public void saveDocuments(Doctorant doctorant, List<DocumentDTO> documentDTOs) {
        for (DocumentDTO dto : documentDTOs) {
            try {
                String fileName = generateFileName(dto.getNomFichier());
                Path filePath = Paths.get(uploadDir, fileName);

                // Créer le répertoire s'il n'existe pas
                Files.createDirectories(filePath.getParent());

                // Sauvegarder le fichier
                byte[] fileContent = Base64.getDecoder().decode(dto.getBase64Content());
                Files.write(filePath, fileContent);

                // Sauvegarder en base
                Document document = new Document();
                document.setNomFichier(dto.getNomFichier());
                document.setTypeDocument(dto.getTypeDocument());
                document.setCheminFichier(filePath.toString());
                document.setTailleFichier((long) fileContent.length);
                document.setDoctorant(doctorant);
                document.setDateDepot(LocalDateTime.now());

                documentRepository.save(document);

            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la sauvegarde du document: " + dto.getNomFichier(), e);
            }
        }
    }

    public List<DocumentDTO> getDocumentsByDoctorant(Long doctorantId) {
        return documentRepository.findByDoctorantId(doctorantId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DocumentDTO> getDocumentsByDoctorantAndType(Long doctorantId, TypeDocument type) {
        return documentRepository.findByDoctorantIdAndTypeDocument(doctorantId, type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteDocumentsByDoctorant(Long doctorantId) {
        documentRepository.deleteByDoctorantId(doctorantId);
    }

    public byte[] getDocumentContent(Long documentId) throws IOException {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

        Path filePath = Paths.get(document.getCheminFichier());
        return Files.readAllBytes(filePath);
    }

    private String generateFileName(String originalFileName) {
        String extension = "";
        int lastDotIndex = originalFileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = originalFileName.substring(lastDotIndex);
        }
        return UUID.randomUUID().toString() + extension;
    }

    private DocumentDTO convertToDTO(Document document) {
        DocumentDTO dto = new DocumentDTO();
        dto.setNomFichier(document.getNomFichier());
        dto.setTypeDocument(document.getTypeDocument());
        return dto;
    }
}