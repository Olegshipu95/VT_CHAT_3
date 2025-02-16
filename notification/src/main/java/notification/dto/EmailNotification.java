package notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotification {
    private final UUID id = UUID.randomUUID();
    private String email;
    private String title;
    private String text;
    private final LocalDateTime localDateTime = LocalDateTime.now();
}
