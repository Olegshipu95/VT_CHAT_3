package feed.service;

import feed.dto.request.CreatePostRequest;
import feed.entity.Post;
import feed.repository.FeedRepository;
import feed.utils.SecurityMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import reactor.test.StepVerifier;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @Mock
    private FeedRepository feedRepository;

    @InjectMocks
    private FeedService feedService;

    @BeforeEach
    public void setUp() {
        SecurityMock.mockSecurityContext();
    }

    @Test
    void testCreateFeed() {
        CreatePostRequest createPostRequest = new CreatePostRequest("Title", "Text", List.of("url"));
        when(feedRepository.save(any(Post.class))).thenReturn(new Post(UUID.randomUUID(), UUID.randomUUID().toString(),
                "Title", "Text", List.of("urls") , Timestamp.valueOf(LocalDateTime.now())));

        StepVerifier.create(feedService.createFeed(createPostRequest))
            .expectNextCount(1)
            .verifyComplete();

        verify(feedRepository, times(1)).save(any());
    }

    @Test
    void testDeletePost() {
        StepVerifier.create(feedService.deletePost(UUID.randomUUID()))
                .expectNextCount(0)
                    .verifyComplete();

        verify(feedRepository, times(1)).deleteById(any());
    }

    @Test
    void testDeletePostNotFound() {
        Pageable pageable = Pageable.ofSize(20);
        Page<Post> expected = new PageImpl<>(List.of(
            new Post(UUID.randomUUID(), "userId", "title", "text", List.of("q"), new Timestamp(System.currentTimeMillis()))));
        when(feedRepository.findByUserId(any(), any())).thenReturn(expected);

        StepVerifier.create(feedService.getFeedByUserId("userId", pageable))
            .expectNextCount(1)
            .verifyComplete();

        verify(feedRepository, times(1)).findByUserId(any(), any());
    }
}