package subscription.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import subscription.utils.ErrorMessages;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscribers")
@Schema
public class Subscribers {
    @Id
    @NotNull(message = ErrorMessages.ID_CANNOT_BE_NULL)
    @Schema(description = "Id подписки")
    private UUID id;

    @Column(name = "user_id")
    @NotNull(message = ErrorMessages.ID_CANNOT_BE_NULL)
    @Schema(description = "Id пользователя")
    private UUID userId;

    @Column(name = "subscribed_user_id")
    @NotNull(message = ErrorMessages.ID_CANNOT_BE_NULL)
    @Schema(description = "Id подписчика")
    private UUID subscribedUserId;

    @Column(name = "subscription_time")
    @NotNull(message = "subscription time can not be null")
    @Schema(description = "Вермя подписки")
    private LocalDateTime subscriptionTime;
}
