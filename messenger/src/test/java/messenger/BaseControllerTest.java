package messenger;

import messenger.config.PostgresTestContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static messenger.utils.SecurityMock.mockSecurityContext;


@SpringBootTest(
    classes = PostgresTestContainer.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BaseControllerTest {

    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build();
        mockSecurityContext();
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }
}