package file.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            //.addServersItem(new Server().url("http://localhost:8080/api"))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(
                new Components().addSecuritySchemes(
                    "Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .bearerFormat("JWT")
                        .scheme("bearer")
                )
            )
            .info(new Info().title("Chat File API").version("1.0"));
    }
}
