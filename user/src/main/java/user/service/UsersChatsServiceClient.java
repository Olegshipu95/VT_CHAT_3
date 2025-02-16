package user.service;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import user.entity.UsersChats;

@FeignClient(name = "chat-messenger-cloud")
public interface UsersChatsServiceClient {

    @Headers("Content-Type: application/json")
    @PostMapping("/chats/users")
    UsersChats save(@RequestBody UsersChats usersChats);
}
