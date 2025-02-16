package notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notification.config.JavaMailSenderProperties;
import notification.dto.EmailNotification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;

    private final JavaMailSenderProperties props;

    public void sendEmail(EmailNotification email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(props.getUsername());
        message.setTo(email.getEmail());
        message.setSubject(email.getTitle());
        message.setText(email.getText());
        log.info("Sending with id: {} email to {}", email.getId(), email.getEmail());
        mailSender.send(message);
    }
}
