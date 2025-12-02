package ma.emsi.inscription_et_reinscription.clients;
import lombok.Data;
@Data
public class UserResponseWrapper {
    private boolean success;
    private String message;
    private UserResponse data;
}
