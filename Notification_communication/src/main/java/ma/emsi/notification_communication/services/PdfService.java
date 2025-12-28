package ma.emsi.notification_communication.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.emsi.notification_communication.dto.DocumentRequest;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfService {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public byte[] generatePdf(String content) {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);

            document.open();
            document.add(new Paragraph(content));
            document.close();

            return out.toByteArray();
        } catch (DocumentException e) {
            log.error("Erreur lors de la génération du PDF: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    public byte[] generateAttestationInscription(String studentName, String studentId, String year) {
        String content = String.format("""
            ATTESTATION D'INSCRIPTION
            
            Je soussigné, certifie que
            L'étudiant(e) : %s
            Numéro d'étudiant : %s
            
            Est régulièrement inscrit(e) en Doctorat
            Pour l'année universitaire : %s
            
            Fait à EMSI, le %s
            """, 
            studentName, 
            studentId, 
            year,
            LocalDateTime.now().format(dateFormatter));
            
        return generatePdf(content);
    }

    public byte[] generateAutorisationSoutenance(String studentName, String thesisTitle, String date) {
        String content = String.format("""
            AUTORISATION DE SOUTENANCE
            
            Je soussigné, autorise
            M./Mme : %s
            
            À soutenir sa thèse de Doctorat intitulée :
            "%s"
            
            Date de soutenance : %s
            
            Fait à EMSI, le %s
            """,
            studentName,
            thesisTitle,
            date,
            LocalDateTime.now().format(dateFormatter));
            
        return generatePdf(content);
    }

    public byte[] generateProcesVerbal(DocumentRequest request) {
        StringBuilder content = new StringBuilder();
        content.append("PROCÈS-VERBAL DE SOUTENANCE DE THÈSE\n\n");
        
        content.append(String.format("Le %s à %s\n\n", 
            request.getSoutenanceDateTime().format(dateFormatter),
            request.getSoutenanceDateTime().format(timeFormatter)));
        
        content.append(String.format("M./Mme %s\n", request.getStudentName()));
        content.append("a soutenu publiquement ses travaux en vue de l'obtention du Doctorat\n");
        content.append(String.format("Titre de la thèse : \"%s\"\n\n", request.getThesisTitle()));
        
        content.append("Jury de soutenance :\n");
        for (DocumentRequest.JuryMember member : request.getJuryMembers()) {
            content.append(String.format("- %s : %s (%s)\n", 
                member.getRole(), 
                member.getName(), 
                member.getInstitution()));
        }
        
        content.append(String.format("\n\nDécision du jury : %s\n", request.getDecision()));
        content.append(String.format("Mention : %s\n\n", request.getMention()));
        
        if (request.getObservations() != null && !request.getObservations().isEmpty()) {
            content.append("Observations du jury :\n");
            for (String observation : request.getObservations()) {
                content.append("- ").append(observation).append("\n");
            }
        }
        
        content.append("\n\nSignatures des membres du jury :");
        
        return generatePdf(content.toString());
    }
}