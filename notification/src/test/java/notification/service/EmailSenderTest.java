package notification.service;

import notification.config.JavaMailSenderProperties;
import notification.dto.EmailNotification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailSenderTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private JavaMailSenderProperties properties;

    @InjectMocks
    private EmailSender emailSender;

    @Test
    public void sendEmail_ok() {
        EmailNotification emailNotification = EmailNotification.builder()
            .email("test@test.com")
            .title("test title")
            .text("test text")
            .build();
        when(properties.getUsername()).thenReturn("email@dog.cp");

        assertDoesNotThrow(() -> emailSender.sendEmail(emailNotification));
    }
}
