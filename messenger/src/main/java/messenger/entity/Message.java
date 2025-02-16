package messenger.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.utils.ErrorMessages;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonDeserialize(using = ChatDeserializer.class)
    private Chat chatId;

    @JoinColumn(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "text", nullable = false)
    @NotNull(message = ErrorMessages.TEXT_CANNOT_BE_NULL)
    private String text;

    @Column(name = "messaged_time")
    private Timestamp timestamp;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "photos")
    private List<String> photos;
}