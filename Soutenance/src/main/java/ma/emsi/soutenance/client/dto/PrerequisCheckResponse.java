package ma.emsi.soutenance.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrerequisCheckResponse {
    private boolean available;
    private boolean publicationsOk;
    private int publicationsCount;
    private boolean conferencesOk;
    private int conferencesCount;
    private boolean formationsOk;
    private int formationsHours;
    private String message;

    public boolean isAllSatisfied() {
        return publicationsOk && conferencesOk && formationsOk;
    }

    public static PrerequisCheckResponse unavailable(String reason) {
        PrerequisCheckResponse response = new PrerequisCheckResponse();
        response.setAvailable(false);
        response.setMessage(reason);
        return response;
    }
}
