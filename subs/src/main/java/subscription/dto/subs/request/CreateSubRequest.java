package subscription.dto.subs.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import subscription.utils.ErrorMessages;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Подписка")
public class CreateSubRequest {
        @Schema(description = "Id пользователя")
        @NotNull(message = ErrorMessages.ID_CANNOT_BE_NULL)
        private UUID subscribedUserId;
}
