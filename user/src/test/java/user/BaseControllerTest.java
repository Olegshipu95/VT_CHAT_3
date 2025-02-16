package user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import user.config.PostgresTestContainer;


@SpringBootTest(
    classes = PostgresTestContainer.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
public abstract class BaseControllerTest {

    @Autowired
    protected WebTestClient client;

}