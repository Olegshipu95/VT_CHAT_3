package feed.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Создание поста")
public class CreatePostRequest {

    @Schema(description = "Заголовок")
    @NotNull(message = "feed title cannot be null")
    private String title;

    @Schema(description = "Текст")
    private String text;

    @Schema(description = "Картинки")
    private List<String> images;
}
