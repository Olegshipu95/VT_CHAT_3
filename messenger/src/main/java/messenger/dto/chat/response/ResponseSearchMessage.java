package messenger.dto.chat.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ResponseSearchMessage {
    List<MessageForResponse> listOfMessages;

    public ResponseSearchMessage(List<MessageForResponse> listOfMessages) {
        this.listOfMessages = listOfMessages;
    }

}
