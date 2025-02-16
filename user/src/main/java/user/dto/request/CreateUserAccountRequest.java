package user.dto.request;

import jakarta.validation.constraints.NotBlank;
import user.entity.Role;

import java.time.LocalDate;

public record CreateUserAccountRequest (
        @NotBlank(message = "Name can't be blank")
        String name,
        @NotBlank(message = "Surname can't be blank")
        String surname,
        @NotBlank(message = "Email can't be blank")
        String email,
        @NotBlank(message = "Password can't be blank")
        String password,
        String briefDescription,
        String city,
        LocalDate birthday,
        String logoUrl,
        Role role
) {
}