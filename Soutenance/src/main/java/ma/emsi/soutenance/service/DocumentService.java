package ma.emsi.soutenance.service;


import ma.emsi.soutenance.model.*;
import ma.emsi.soutenance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final SoutenanceRepository soutenanceRepository;
    private final Path rootLocation = Paths.get("uploads/soutenances");

    @Transactional
    public Document uploadDocument(Long soutenanceId, String typeDocument,
                                             MultipartFile fichier, String depotPar) throws IOException {

        Soutenance soutenance = soutenanceRepository.findById(soutenanceId)
                .orElseThrow(() -> new RuntimeException("Soutenance non trouvée"));

        // Vérifier le type de fichier (CdC: PDF, JPG...)
        String contentType = fichier.getContentType();
        if (!estTypeAccepte(contentType)) {
            throw new IllegalArgumentException("Type de fichier non accepté: " + contentType);
        }

        // Vérifier la taille
        if (fichier.getSize() > 10 * 1024 * 1024) { // 10MB max
            throw new IllegalArgumentException("Fichier trop volumineux");
        }

        // Générer nom unique
        String nomOriginal = fichier.getOriginalFilename();
        if (nomOriginal == null || nomOriginal.isBlank()) {
            nomOriginal = typeDocument + "-" + System.currentTimeMillis();
        }
        String extension = "";
        int dotIndex = nomOriginal.lastIndexOf(".");
        if (dotIndex >= 0) {
            extension = nomOriginal.substring(dotIndex);
        }
        String nomUnique = UUID.randomUUID().toString() + extension;

        // Créer le dossier si nécessaire
        Files.createDirectories(rootLocation);

        // Sauvegarder le fichier
        Path destination = rootLocation.resolve(nomUnique);
        Files.copy(fichier.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        // Créer l'entité Document
        Document document = new Document();
        document.setSoutenance(soutenance);
        document.setTypeDocument(typeDocument);
        document.setNomFichier(nomOriginal);

        // IMPORTANT : remplir cheminFichier (ce setter met aussi cheminStockage)
        document.setCheminFichier(destination.toString());

        document.setTailleOctets(fichier.getSize());
        document.setContentType(contentType);
        document.setDateDepot(LocalDateTime.now());
        document.setDepotPar(depotPar);
        document.setStatutValidation("EN_ATTENTE");

        return documentRepository.save(document);
    }

    private boolean estTypeAccepte(String contentType) {
        if (contentType == null) {
            return false;
        }
        return contentType.equals("application/pdf") ||
            contentType.startsWith("image/") ||
            contentType.equals("application/msword") ||
            contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
            contentType.equals("application/vnd.oasis.opendocument.text");
    }

    @Transactional
    public Document validerDocument(Long documentId, boolean valide, String commentaire) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

        document.setStatutValidation(valide ? "VALIDE" : "REJETE");
        document.setCommentaireValidation(commentaire);

        return documentRepository.save(document);
    }

    public List<Document> getDocumentsBySoutenance(Long soutenanceId) {
        return documentRepository.findBySoutenanceId(soutenanceId);
    }

    public Document getDocumentById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));
    }

    public byte[] downloadDocument(Long documentId) throws IOException {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));

        return Files.readAllBytes(Paths.get(document.getCheminStockage()));
    }
}