package messenger.dto.chat.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.entity.ChatType;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatForResponse {
    private UUID id;
    private ChatType chatType;
    private int countMembers;
    private String lastMessage;
    private boolean lastMessageHavePhoto;
}
