package ma.emsi.inscription_et_reinscription.clients;

import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public UserDTO getUserById(Long id) {
        UserDTO dto = new UserDTO();
        dto.setId(id);
        dto.setUsername("unknown");
        dto.setEmail("unknown@example.com");
        return dto;
    }
}

