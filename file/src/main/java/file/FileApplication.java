package file;

import file.config.S3Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableConfigurationProperties(S3Properties.class)
public class FileApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileApplication.class, args);
	}

}
