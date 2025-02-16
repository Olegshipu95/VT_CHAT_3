package user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Schema
public record GetUserInfoResponse(
    @Schema(description = "Id пользователя")
    @NotNull(message = "Id can't be null")
    UUID userid,
    @Schema(description = "Имя пользователя")
    @NotBlank(message = "Name can't be blank")
    String name,
    @Schema(description = "Фамилия пользователя")
    @NotBlank(message = "Surname can't be blank")
    String surname,
    @Schema(description = "Почта пользователя")
    @NotBlank(message = "Email can't be blank")
    String email,
    @Schema(description = "Описание пользователя")
    String briefDescription,
    @Schema(description = "Город пользователя")
    String city,
    @Schema(description = "День рождения пользователя")
    LocalDate birthday,
    @Schema(description = "Аватарка url")
    String logoUrl
) {
}
