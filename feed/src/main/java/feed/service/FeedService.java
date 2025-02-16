package feed.service;

import feed.dto.request.CreatePostRequest;
import feed.entity.Post;
import feed.repository.FeedRepository;
import feed.utils.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.sql.Timestamp;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;

    public Flux<Post> getFeedByUserId(String userId, Pageable pageable) {
        log.debug("GET: start getFeedByUserId for userId: {}", userId);
        return Mono.fromCallable(() -> feedRepository.findByUserId(userId, pageable))
            .subscribeOn(Schedulers.boundedElastic())
            .flatMapMany(Flux::fromIterable);
    }

    public Mono<UUID> createFeed(CreatePostRequest createPostRequest) {
        String userId = SecurityContextHolder.getUserId();
        log.debug("CREATE: start createFeed with userId: {}", userId);
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setUserId(userId);
        post.setTitle(createPostRequest.getTitle());
        post.setText(createPostRequest.getText());
        post.setImagesUrls(createPostRequest.getImages());
        post.setPostedTime(new Timestamp(System.currentTimeMillis()));
        return Mono.fromCallable(() -> feedRepository.save(post))
            .subscribeOn(Schedulers.boundedElastic())
            .map(Post::getId);
    }

    public Mono<Void> deletePost(UUID feedID) {
        log.debug("DELETE: start with feedId: {}", feedID);
        return Mono.fromRunnable(() -> feedRepository.deleteById(feedID))
            .subscribeOn(Schedulers.boundedElastic())
            .then();
    }
}
