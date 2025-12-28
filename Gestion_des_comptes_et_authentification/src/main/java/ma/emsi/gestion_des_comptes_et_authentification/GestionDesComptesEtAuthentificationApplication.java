package ma.emsi.gestion_des_comptes_et_authentification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GestionDesComptesEtAuthentificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionDesComptesEtAuthentificationApplication.class, args);
    }

}
