package file.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "spring.s3")
public class S3Properties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String region;
}
