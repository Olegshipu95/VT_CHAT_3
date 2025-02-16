package messenger.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.utils.ErrorMessages;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chats")
@Schema
public class Chat {

    @Id
    @Schema(description = "ID чата")
    private UUID id;

    @Schema(description = "Имя чата")
    @Column(name = "name", nullable = false)
    @NotNull(message = ErrorMessages.NAME_CANNOT_BE_NULL)
    private String name;

    @Schema(description = "Тип чата 0 или 1")
    @Column(name = "chat_type", nullable = false)
    @NotNull(message = "chatType cannot be null")
    @Min(value = 0, message = "chatType has not this meaning")
    @Max(value = 1, message = "chatType has not this meaning")
    private int chatType;
}

