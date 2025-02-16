package feed.controller;

import feed.BaseControllerTest;
import feed.dto.request.CreatePostRequest;
import feed.utils.MappingUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeedControllerTest extends BaseControllerTest {

    @Test
    public void getPostsPage() throws Exception {
        mockMvc.perform(
            get("/feed/" + UUID.randomUUID())
                .param("page", "0")
                .param("size", "20")
        ).andExpectAll(status().isOk());
    }

    @Test
    public void createPost() throws Exception {
        mockMvc.perform(
            post("/feed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MappingUtils.toJson(new CreatePostRequest("title", "text", List.of("qwe"))))
        ).andExpectAll(status().isCreated());
    }

    @Test
    public void deletePost() throws Exception {
        mockMvc.perform(
            delete("/feed/" + UUID.randomUUID())
        ).andExpectAll(status().isNoContent());
    }
}
