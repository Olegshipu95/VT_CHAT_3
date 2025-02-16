package subscription.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import subscription.dto.EmailNotification;

import static subscription.utils.MappingUtils.toJson;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaNotificationProducer {

    @Value("${spring.kafka.topics.notification-email-topic}")
    private String notificationTopic;

    private final KafkaTemplate<String, String> kafkaNotificationTemplate;

    public void sendEmailNotification(EmailNotification email) {
        log.info("Sending email: {} to {}", email, notificationTopic);
        kafkaNotificationTemplate.send(notificationTopic, email.getId().toString(), toJson(email))
            .whenComplete(this::addCallBack);
    }

    private void addCallBack(SendResult<String, String> result, Throwable ex) {
        if (ex == null) {
            log.info("Message with id - {} sent successfully", result.getProducerRecord().key());
        } else {
            log.warn("Message with id - {} sent failed, because - {}", result.getProducerRecord().key(), ex.getMessage());
        }
    }
}
