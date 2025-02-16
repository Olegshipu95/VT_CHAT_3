package subscription.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    private static final int PARTITIONS = 1;
    private static final int REPLICAS = 3;
    private static final String SYNC_REPLICAS = "2";
    private static final String MIN_SYNC_REPLICAS = "min.insync.replicas";

    @Bean
    public NewTopic notificationEmailTopic(
        @Value("${spring.kafka.topics.notification-email-topic}") String notificationEmailTopic
    ) {
        return TopicBuilder.name(notificationEmailTopic)
            .partitions(PARTITIONS)
            .replicas(REPLICAS)
            .configs(Map.of(MIN_SYNC_REPLICAS, SYNC_REPLICAS))
            .build();
    }
}
