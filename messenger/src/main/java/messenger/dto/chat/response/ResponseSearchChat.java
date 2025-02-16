package messenger.dto.chat.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema
public class ResponseSearchChat {
    @Schema(description = "Чаты")
    public List<ChatForResponse> response;

    public void setResponse(List<ChatForResponse> response) {
        this.response = response;
    }

}
