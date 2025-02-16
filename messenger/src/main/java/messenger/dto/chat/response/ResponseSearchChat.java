package messenger.dto.chat.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ResponseSearchChat {
    public List<ChatForResponse> response;

    public void setResponse(List<ChatForResponse> response) {
        this.response = response;
    }

}
