package ma.emsi.soutenance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing  // Ajoutez cette ligne
@EnableFeignClients
public class SoutenanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoutenanceApplication.class, args);
    }

}
