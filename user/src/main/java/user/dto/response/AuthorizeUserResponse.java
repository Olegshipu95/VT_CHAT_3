package user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record AuthorizeUserResponse(
    @Schema(description = "Id пользователя") UUID id,
    @Schema(description = "Логин пользователя") String username,
    @Schema(description = "Access токен") String access,
    @Schema(description = "Refresh токен") String refresh
) {}
