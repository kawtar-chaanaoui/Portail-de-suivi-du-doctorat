package ma.emsi.inscription_et_reinscription.clients;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Data

public class UserResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private UserRole role;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private String specialty;
    private String laboratory;
    private String studentId;
}
