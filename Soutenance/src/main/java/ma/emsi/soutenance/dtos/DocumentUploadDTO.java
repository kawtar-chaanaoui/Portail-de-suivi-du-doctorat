package ma.emsi.soutenance.dtos;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DocumentUploadDTO {
    private Long soutenanceId;
    private String typeDocument;
    private MultipartFile fichier;
    private String depotPar;
}
