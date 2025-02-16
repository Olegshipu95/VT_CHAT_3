package subscription.dto.subs.response;

import io.swagger.v3.oas.annotations.media.Schema;
import subscription.utils.ErrorMessages;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Ответ подписки")
public record SubscriptionResponse (
        @NotNull(message = ErrorMessages.ID_CANNOT_BE_NULL)
       @Schema(description = "Id подписки")
        UUID id,

        @NotNull(message = ErrorMessages.ID_CANNOT_BE_NULL)
        @JsonProperty("subscribed_user_id")
        @Schema(description = "Id подписчика")
        UUID subscribedUserId,

        @NotNull(message = "subscription time can not be null")
        @JsonProperty("subscription_time")
        @Schema(description = "Время подписки")
        LocalDateTime subscriptionTime
) {
}