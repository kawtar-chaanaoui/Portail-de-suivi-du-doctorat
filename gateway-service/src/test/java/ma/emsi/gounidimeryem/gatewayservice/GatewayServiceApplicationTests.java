package ma.emsi.gatewayservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = GatewayServiceApplication.class, properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.config.import-check.enabled=false"
})
class GatewayServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
