package notification.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notification.dto.EmailNotification;
import notification.service.EmailSender;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static notification.utils.MappingUtils.toObject;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final EmailSender emailSender;

    @KafkaListener(
        groupId = "${spring.kafka.consumer.group-id}",
        topics = "${spring.kafka.topics.notification-email-topic}"
    )
    public void consume(String email) {
        log.info("Consumed email: {}", email);
        emailSender.sendEmail(toObject(email, new TypeReference<EmailNotification>() {}));
    }
}
