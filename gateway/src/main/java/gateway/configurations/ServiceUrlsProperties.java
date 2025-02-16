package gateway.configurations;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "spring.services.urls")
public class ServiceUrlsProperties {

    private final String user;
    private final String feed;
    private final String messenger;
    private final String subs;
    private final String file;

    public ServiceUrlsProperties(String user, String feed, String messenger, String subs, String file) {
        this.user = user;
        this.feed = feed;
        this.messenger = messenger;
        this.subs = subs;
        this.file = file;
    }
}