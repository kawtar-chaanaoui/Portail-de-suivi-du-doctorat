package ma.emsi.inscription_et_reinscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InscriptionEtReinscriptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(InscriptionEtReinscriptionApplication.class, args);
    }

}
