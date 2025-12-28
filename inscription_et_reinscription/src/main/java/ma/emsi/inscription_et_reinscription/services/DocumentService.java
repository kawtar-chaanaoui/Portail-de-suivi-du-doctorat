package ma.emsi.inscription_et_reinscription.services;

import ma.emsi.inscription_et_reinscription.dtos.DocumentDTO;
import ma.emsi.inscription_et_reinscription.entities.Document;
import ma.emsi.inscription_et_reinscription.entities.Doctorant;
import ma.emsi.inscription_et_reinscription.entities.TypeDocument;
import ma.emsi.inscription_et_reinscription.exceptions.DocumentInvalideException;
import ma.emsi.inscription_et_reinscription.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentService {

    // Formats de fichiers acceptés
    private static final List<String> EXTENSIONS_ACCEPTEES = Arrays.asList(
            ".pdf", ".PDF",
            ".jpg", ".JPG", ".jpeg", ".JPEG",
            ".png", ".PNG"
    );

    // Types MIME acceptés
    private static final Map<String, List<String>> MIME_TYPES = new HashMap<>() {{
        put("application/pdf", Arrays.asList(".pdf", ".PDF"));
        put("image/jpeg", Arrays.asList(".jpg", ".JPG", ".jpeg", ".JPEG"));
        put("image/png", Arrays.asList(".png", ".PNG"));
    }};

    // Taille maximale par fichier : 10 MB
    private static final long TAILLE_MAX_BYTES = 10 * 1024 * 1024;

    @Autowired
    private DocumentRepository documentRepository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    @Value("${app.upload.max-size:10485760}")
    private long tailleMaxBytes;

    public void saveDocuments(Doctorant doctorant, List<DocumentDTO> documentDTOs) {
        for (DocumentDTO dto : documentDTOs) {
            try {
                // Validation du nom de fichier
                if (dto.getNomFichier() == null || dto.getNomFichier().trim().isEmpty()) {
                    throw new DocumentInvalideException("Le nom du fichier est obligatoire");
                }

                // Décoder le contenu Base64
                byte[] fileContent;
                try {
                    fileContent = Base64.getDecoder().decode(dto.getBase64Content());
                } catch (IllegalArgumentException e) {
                    throw new DocumentInvalideException("Le contenu du fichier est invalide (encodage Base64 requis)", e);
                }

                // Valider le format du fichier
                validerFormatFichier(dto.getNomFichier());

                // Valider la taille du fichier
                validerTailleFichier(fileContent.length, dto.getNomFichier());

                // Valider le type MIME à partir des magic bytes
                validerTypeMime(fileContent, dto.getNomFichier());

                // Générer un nom de fichier sécurisé
                String fileName = generateFileName(dto.getNomFichier());
                Path filePath = Paths.get(uploadDir, fileName);

                // Créer le répertoire s'il n'existe pas
                Files.createDirectories(filePath.getParent());

                // Sauvegarder le fichier
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

    /**
     * Valider le format du fichier par son extension
     */
    private void validerFormatFichier(String nomFichier) {
        String extension = getExtension(nomFichier);
        
        if (extension.isEmpty()) {
            throw new DocumentInvalideException(
                    "Le fichier '" + nomFichier + "' n'a pas d'extension");
        }

        if (!EXTENSIONS_ACCEPTEES.contains(extension)) {
            throw new DocumentInvalideException(
                    "Format de fichier non accepté pour '" + nomFichier + "'. " +
                    "Formats acceptés : PDF, JPG, JPEG, PNG");
        }
    }

    /**
     * Valider la taille du fichier
     */
    private void validerTailleFichier(long taille, String nomFichier) {
        long tailleMax = tailleMaxBytes > 0 ? tailleMaxBytes : TAILLE_MAX_BYTES;
        
        if (taille > tailleMax) {
            double tailleMo = taille / (1024.0 * 1024.0);
            double tailleMaxMo = tailleMax / (1024.0 * 1024.0);
            throw new DocumentInvalideException(
                    String.format("Le fichier '%s' est trop volumineux (%.2f MB). Taille maximale : %.2f MB",
                            nomFichier, tailleMo, tailleMaxMo));
        }

        if (taille == 0) {
            throw new DocumentInvalideException("Le fichier '" + nomFichier + "' est vide");
        }
    }

    /**
     * Valider le type MIME du fichier à partir des magic bytes
     */
    private void validerTypeMime(byte[] fileContent, String nomFichier) {
        String detectedMimeType = detectMimeType(fileContent);
        String extension = getExtension(nomFichier);

        if (detectedMimeType == null) {
            throw new DocumentInvalideException(
                    "Impossible de déterminer le type du fichier '" + nomFichier + "'");
        }

        // Vérifier que le type MIME correspond à l'extension
        List<String> extensionsAttendues = MIME_TYPES.get(detectedMimeType);
        if (extensionsAttendues == null || !extensionsAttendues.contains(extension)) {
            throw new DocumentInvalideException(
                    "Le contenu du fichier '" + nomFichier + "' ne correspond pas à son extension. " +
                    "Type détecté : " + detectedMimeType);
        }
    }

    /**
     * Détecter le type MIME à partir des magic bytes
     */
    private String detectMimeType(byte[] fileContent) {
        if (fileContent.length < 4) {
            return null;
        }

        // PDF : %PDF (0x25 0x50 0x44 0x46)
        if (fileContent[0] == 0x25 && fileContent[1] == 0x50 &&
            fileContent[2] == 0x44 && fileContent[3] == 0x46) {
            return "application/pdf";
        }

        // JPEG : 0xFF 0xD8 0xFF
        if (fileContent[0] == (byte) 0xFF && fileContent[1] == (byte) 0xD8 &&
            fileContent[2] == (byte) 0xFF) {
            return "image/jpeg";
        }

        // PNG : 0x89 0x50 0x4E 0x47 0x0D 0x0A 0x1A 0x0A
        if (fileContent.length >= 8 &&
            fileContent[0] == (byte) 0x89 && fileContent[1] == 0x50 &&
            fileContent[2] == 0x4E && fileContent[3] == 0x47 &&
            fileContent[4] == 0x0D && fileContent[5] == 0x0A &&
            fileContent[6] == 0x1A && fileContent[7] == 0x0A) {
            return "image/png";
        }

        return null;
    }

    /**
     * Extraire l'extension d'un nom de fichier
     */
    private String getExtension(String nomFichier) {
        int lastDotIndex = nomFichier.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < nomFichier.length() - 1) {
            return nomFichier.substring(lastDotIndex);
        }
        return "";
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

    /**
     * Valider un document sans le sauvegarder (pour pré-validation)
     */
    public void validerDocument(DocumentDTO documentDTO) {
        // Validation du nom de fichier
        if (documentDTO.getNomFichier() == null || documentDTO.getNomFichier().trim().isEmpty()) {
            throw new DocumentInvalideException("Le nom du fichier est obligatoire");
        }

        // Décoder le contenu Base64
        byte[] fileContent;
        try {
            fileContent = Base64.getDecoder().decode(documentDTO.getBase64Content());
        } catch (IllegalArgumentException e) {
            throw new DocumentInvalideException("Le contenu du fichier est invalide (encodage Base64 requis)", e);
        }

        // Valider le format du fichier
        validerFormatFichier(documentDTO.getNomFichier());

        // Valider la taille du fichier
        validerTailleFichier(fileContent.length, documentDTO.getNomFichier());

        // Valider le type MIME à partir des magic bytes
        validerTypeMime(fileContent, documentDTO.getNomFichier());
    }

    private String generateFileName(String originalFileName) {
        String extension = getExtension(originalFileName);
        return UUID.randomUUID().toString() + extension;
    }

    private DocumentDTO convertToDTO(Document document) {
        DocumentDTO dto = new DocumentDTO();
        dto.setNomFichier(document.getNomFichier());
        dto.setTypeDocument(document.getTypeDocument());
        return dto;
    }
}