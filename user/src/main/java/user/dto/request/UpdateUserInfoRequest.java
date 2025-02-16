package user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import user.utils.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Schema
public record UpdateUserInfoRequest(
        @NotNull(message = ErrorMessages.ID_CANNOT_BE_NULL)
        @Schema(description = "Id пользователя")
        UUID userId,
        @NotBlank(message = "Name can't be blank")
        @Schema(description = "Имя")
        String name,
        @NotBlank(message = "Surname can't be blank")
        @Schema(description = "Фамилия")
        String surname,
        @Schema(description = "Почта")
        @NotBlank(message = "Email can't be blank")
        String email,
        @Schema(description = "Описание")
        String briefDescription,
        @Schema(description = "Город")
        String city,
        @Schema(description = "День рождения")
        LocalDate birthday,
        @Schema(description = "Аватарка")
        String logoUrl) { }