package ma.emsi.inscription_et_reinscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("ma.emsi.inscription_et_reinscription.entities")
@EnableJpaRepositories("ma.emsi.inscription_et_reinscription.repositories")
public class InscriptionEtReinscriptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(InscriptionEtReinscriptionApplication.class, args);

    }

}
