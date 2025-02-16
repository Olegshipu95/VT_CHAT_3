package user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record AuthorizeUserRequest(
    @Schema(description = "Логин") @Size(min = 9, max = 64) String username,
    @Schema(description = "Пароль") @Size(min = 5, max = 60) String password
) {}

