package user.config.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.security.jwt")
public class AuthenticationProperties {
    private String secret;
    private Long access;
    private Long refresh;
}
