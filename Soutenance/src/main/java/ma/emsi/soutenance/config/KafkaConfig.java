package ma.emsi.soutenance.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(name = "spring.kafka.bootstrap-servers")
public class KafkaConfig {

    @Bean
    public NewTopic soutenanceTopic() {
        return TopicBuilder.name("notifications-soutenance")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic adminTopic() {
        return TopicBuilder.name("notifications-admin")
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic juryTopic() {
        return TopicBuilder.name("notifications-jury")
                .partitions(2)
                .replicas(1)
                .build();
    }
}