
package ma.emsi.inscription_et_reinscription.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/users")
public interface Userclient {

    @GetMapping("/{id}")
    UserResponseWrapper getUserById(@PathVariable("id") String id);

}
