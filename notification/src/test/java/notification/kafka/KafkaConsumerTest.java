package notification.kafka;

import notification.dto.EmailNotification;
import notification.service.EmailSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static notification.utils.MappingUtils.toJson;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerTest {

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private KafkaConsumer kafkaConsumer;

    @Test
    public void consume_ok() {
        EmailNotification emailNotification = EmailNotification.builder()
            .email("test@test.com")
            .title("test title")
            .text("test text")
            .build();
        assertDoesNotThrow(() -> kafkaConsumer.consume(toJson(emailNotification)));
    }
}
