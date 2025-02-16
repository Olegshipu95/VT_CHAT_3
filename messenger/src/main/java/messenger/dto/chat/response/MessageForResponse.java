package messenger.dto.chat.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.entity.Chat;
import messenger.entity.Message;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class MessageForResponse {
    @Schema(description = "ID смс")
    private UUID id;
    @Schema(description = "ID чата")
    private Chat chatId;
    @Schema(description = "ID автора")
    private UUID authorId;
    @Schema(description = "Текст")
    private String text;
    @Schema(description = "Время")
    private Timestamp timestamp;
    @Schema(description = "Фотки")
    private List<String> photos;

    public MessageForResponse(Message message) {
        this.id = message.getId();
        this.chatId = message.getChatId();
        this.authorId = message.getAuthorId();
        this.text = message.getText();
        this.timestamp = message.getTimestamp();
        this.photos = new ArrayList<>();
    }
}
