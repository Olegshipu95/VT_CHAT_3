package subscription.service;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import subscription.dto.User;

import java.util.UUID;

@FeignClient(name = "chat-user-cloud")
public interface UserService {

    @GetMapping("/accounts/users/{id}")
    @Headers("Content-Type: application/json")
    User findById(@PathVariable UUID id);
}
