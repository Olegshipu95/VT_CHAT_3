package user.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import user.BaseControllerTest;
import user.dto.request.CreateUserAccountRequest;
import user.entity.Role;

import java.time.LocalDate;

public class UserControllerTest extends BaseControllerTest {

    //@Test
    //@Disabled
    public void createAccountTest_OK() {
        client.post()
            .uri("/accounts/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new CreateUserAccountRequest(
                "name",
                "surname",
                "email",
                "password",
                "briefDescription",
                "City",
                LocalDate.now(),
                "logo_url",
                Role.SUPERVISOR
            ))
            .exchange()
            .expectStatus().isOk();
    }
}
