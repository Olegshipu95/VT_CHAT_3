package messenger.dto.chat.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Добавление чата")
public class CreateChatRequest {
    @NotNull(message = "chatType cannot be null")
    @Min(value = 0, message = "chatType has not this meaning")
    @Max(value = 1, message = "chatType has not this meaning")
    @Schema(description = "тип чата")
    private int chatType;
    @Schema(description = "имя чата")
    private String name;
}
