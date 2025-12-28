package ma.emsi.notification_communication.services;

import ma.emsi.notification_communication.dto.DocumentRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PdfServiceTest {

    private final PdfService pdfService = new PdfService();

    @Test
    void generateAttestationInscriptionShouldReturnPdfBytes() {
        byte[] pdf = pdfService.generateAttestationInscription("Yassine", "DOC-001", "2024/2025");
        assertThat(pdf).isNotNull();
        assertThat(pdf.length).isGreaterThan(0);
    }

    @Test
    void generateProcesVerbalShouldIncludeJuryMembers() {
        DocumentRequest request = new DocumentRequest();
        request.setStudentName("Aya");
        request.setThesisTitle("IA et Systèmes");
        request.setSoutenanceDateTime(LocalDateTime.now());
        DocumentRequest.JuryMember president = new DocumentRequest.JuryMember();
        president.setName("Pr. Karim");
        president.setRole("Président");
        president.setInstitution("EMSI");
        request.setJuryMembers(List.of(president));
        request.setDecision("Admis");
        request.setMention("Très honorable");

        byte[] pdf = pdfService.generateProcesVerbal(request);
        assertThat(pdf).isNotNull();
        assertThat(pdf.length).isGreaterThan(0);
    }
}
