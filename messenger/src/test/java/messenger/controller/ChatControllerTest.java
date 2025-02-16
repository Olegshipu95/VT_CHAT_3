package messenger.controller;

import messenger.BaseControllerTest;
import messenger.dto.chat.request.CreateChatRequest;
import messenger.entity.UsersChats;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static messenger.utils.MappingUtils.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ChatControllerTest extends BaseControllerTest {


    @Test
    public void createUsersChats() throws Exception {
        mockMvc.perform(
            post("/chats/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(new UsersChats(UUID.randomUUID(), UUID.fromString("7f15eb05-2560-4af0-a25f-9139667fde93"), List.of())))
        ).andExpect(status().isCreated());
    }

    @Test
    public void createChat_not_found() throws Exception {
        mockMvc.perform(
            post("/chats")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(new CreateChatRequest(0, "name", List.of(UUID.fromString("7f15eb05-2560-4af0-a25f-9139667fde93"), UUID.randomUUID()))))
        ).andExpect(status().isNotFound());
    }
}
