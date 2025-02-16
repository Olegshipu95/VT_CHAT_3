package user.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.PostgreSQLR2DBCDatabaseContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class PostgresTestContainer {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("user");
    }

    @Bean
    @ServiceConnection
    public PostgreSQLR2DBCDatabaseContainer r2dbcContainer(PostgreSQLContainer<?> postgresContainer) {
        return new PostgreSQLR2DBCDatabaseContainer(postgresContainer);
    }
}
