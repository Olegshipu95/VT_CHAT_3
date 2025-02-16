package file;

import file.utils.SecurityMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseControllerTest {

	protected MockMvc mockMvc;

	@BeforeEach
	void setUp(WebApplicationContext context) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		SecurityMock.mockSecurityContext();
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	private static final MinIOContainer minioContainer = new MinIOContainer(
		DockerImageName.parse("minio/minio:RELEASE.2024-08-03T04-33-23Z")
	);

	@BeforeAll
	static void startContainers() {
		minioContainer.start();
	}

	@AfterAll
	static void stopContainers() {
		minioContainer.stop();
	}

	@DynamicPropertySource
	static void registerProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.s3.endpoint", minioContainer::getS3URL);
		registry.add("spring.s3.access-key", minioContainer::getUserName);
		registry.add("spring.s3.secret-key", minioContainer::getPassword);
		registry.add("spring.s3.bucket", () -> "chat");
		registry.add("spring.s3.region", () -> "us-east-1");
	}
}
