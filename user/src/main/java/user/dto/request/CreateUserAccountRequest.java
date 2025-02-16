package user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import user.entity.Role;

import java.time.LocalDate;

@Schema
public record CreateUserAccountRequest (
        @NotBlank(message = "Name can't be blank")
        @Schema(description = "ИМя")
        String name,
        @NotBlank(message = "Surname can't be blank")
        @Schema(description = "Фамилия")
        String surname,
        @Schema(description = "Почта")
        @NotBlank(message = "Email can't be blank")
        String email,
        @Schema(description = "Пароль")
        @NotBlank(message = "Password can't be blank")
        String password,
        @Schema(description = "Описание")
        String briefDescription,
        @Schema(description = "Город")
        String city,
        @Schema(description = "День рождения")
        LocalDate birthday,
        @Schema(description = "Аватарка")
        String logoUrl,
        @Schema(description = "Роль")
        Role role
) {
}