package notification.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.mail")
public class JavaMailSenderProperties {
    private  String host;
    private  int port;
    private  String username;
    private  String password;
    private  String protocol;
    private  String debug;
}
