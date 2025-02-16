package feed.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreatePostRequest {

    @NotNull(message = "feed title cannot be null")
    private String title;

    private String text;

    private List<String> images;
}
