package messenger.dto.chat.response;

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
public class MessageForResponse {
    private UUID id;
    private Chat chatId;
    private UUID authorId;
    private String text;
    private Timestamp timestamp;
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
