package feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "title")
    private String title;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "images")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> imagesUrls;

    @Column(name = "posted_time", nullable = false)
    private Timestamp postedTime;
}
