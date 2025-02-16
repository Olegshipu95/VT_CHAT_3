package messenger.dto.chat.response;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.entity.ChatType;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class ChatForResponse {

    @Schema(description = "iD чата")
    private UUID id;
    @Schema(description = "Тип чата")
    private ChatType chatType;
    @Schema(description = "Количество человек")
    private int countMembers;
    @Schema(description = "последнее сообщение")
    private String lastMessage;
    @Hidden
    private boolean lastMessageHavePhoto;
}
