package notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Map;

@Configuration
public class JavaMailSenderConfig {


    @Bean
    public JavaMailSender javaMailSender(JavaMailSenderProperties props) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(props.getHost());
        mailSender.setPort(props.getPort());
        mailSender.setUsername(props.getUsername());
        mailSender.setPassword(props.getPassword());
        mailSender.getJavaMailProperties().putAll(
            Map.of(
                "mail.transport.protocol", props.getProtocol(),
                "mail.debug", props.getDebug()
            )
        );
        return mailSender;
    }
}
