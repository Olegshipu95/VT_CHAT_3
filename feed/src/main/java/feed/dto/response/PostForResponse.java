package feed.dto.response;

import feed.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PostForResponse{

    private UUID id;
    private UUID userId;
    private String title;
    private String text;
    private List<String> images;
    private Timestamp postedTime;

    public PostForResponse(Post post){
        this.id = post.getId();
        this.userId = post.getId();
        this.title = post.getTitle();
        this.text = post.getText();
        this.images = post.getImagesUrls();
        this.postedTime = post.getPostedTime();
    }
}
