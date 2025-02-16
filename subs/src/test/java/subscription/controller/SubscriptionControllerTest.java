package subscription.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subscription.BaseControllerTest;
import subscription.dto.subs.request.CreateSubRequest;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static subscription.utils.MappingUtils.toJson;

class SubscriptionControllerTest extends BaseControllerTest {

    @Test
    public void createSubs_BAD() throws Exception {
        mockMvc.perform(
            post("/subscribe")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(new CreateSubRequest(UUID.randomUUID())))
        ).andExpectAll(status().isBadRequest());
    }

    @Test
    public void deleteSubs_NOT_FOUND() throws Exception {
        mockMvc.perform(
            delete("/subscribe/" + UUID.randomUUID())
        ).andExpectAll(status().isNotFound());
    }
}