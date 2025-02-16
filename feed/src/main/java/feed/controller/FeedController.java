package feed.controller;

import feed.dto.request.CreatePostRequest;
import feed.entity.Post;
import feed.service.FeedService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;

    @PageableAsQueryParam
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Post> getAllPostsByUserId(
        @PathVariable String userId,
        @Schema(hidden = true) @PageableDefault(size = 50) Pageable pageable
    ) {
        return feedService.getFeedByUserId(userId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UUID> createPost(@Valid @RequestBody CreatePostRequest createPostRequest) {
        return feedService.createFeed(createPostRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deletePost(@PathVariable UUID id) {
        return feedService.deletePost(id);
    }
}
