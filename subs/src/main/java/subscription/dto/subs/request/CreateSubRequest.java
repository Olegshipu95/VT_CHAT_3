package subscription.dto.subs.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import subscription.utils.ErrorMessages;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubRequest {
        @NotNull(message = ErrorMessages.ID_CANNOT_BE_NULL)
        private UUID subscribedUserId;
}
