package ma.emsi.inscription_et_reinscription;

import ma.emsi.inscription_et_reinscription.clients.Userclient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "ma.emsi.inscription_et_reinscription.clients")
@SpringBootApplication
@EnableDiscoveryClient
public class InscriptionEtReinscriptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(InscriptionEtReinscriptionApplication.class, args);
    }

    @Autowired
    private Userclient userClient;

}
