package messenger.dto.chat.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.utils.ErrorMessages;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMessageRequest {

    @NotNull(message = ErrorMessages.ID_CANNOT_BE_NULL)
    @Min(value = 0, message = ErrorMessages.ID_CANNOT_BE_NEGATIVE)
    private UUID chatId;

    @NotNull(message = ErrorMessages.REQUEST_CANNOT_BE_NULL)
    private String request;

    @NotNull(message = ErrorMessages.PAGE_CANNOT_BE_NULL)
    @Min(value = 0, message = ErrorMessages.PAGE_CANNOT_BE_NEGATIVE)
    private Long pageNumber = 0L;

    @NotNull(message = ErrorMessages.COUNT_PAGE_CANNOT_BE_NULL)
    @Min(value = 0, message = ErrorMessages.COUNT_PAGE_CANNOT_BE_NEGATIVE)
    private Long countMessagesOnPage = 20L;
}
