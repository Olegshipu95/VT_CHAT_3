package messenger.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messenger.dto.chat.request.CreateChatRequest;
import messenger.dto.chat.response.MessageForResponse;
import messenger.entity.Message;
import messenger.entity.UsersChats;
import messenger.service.ChatService;
import messenger.utils.ErrorMessages;
import messenger.utils.SecurityContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<?> createChat(@Valid @RequestBody CreateChatRequest createChatRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createChat(createChatRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getChat(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.findById(id));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> deleteChat(@NotNull(message = ErrorMessages.ID_CANNOT_BE_NULL) @PathVariable UUID chatId) {
        chatService.deleteChat(chatId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUserChats(@RequestBody UsersChats usersChats) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.addUserChats(usersChats));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsersChats(
        @Param("request") String request,
        @Schema(hidden = true) @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(chatService.getUsersChats(UUID.fromString(SecurityContextHolder.getUserId()), request, (long) pageable.getPageNumber(), (long) pageable.getPageSize()));
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<?> sendMessage(
        @PathVariable UUID id,
        @Valid @RequestBody Message message
    ) {
        UUID response = chatService.sendMessage(id, message);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<?> getMessage(
        @PathVariable UUID id,
        @Param("request") String request,
        @Schema(hidden = true) @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(chatService.searchMessage(id, request, pageable));
    }

    @GetMapping(value = "/subscribe/{chatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<MessageForResponse> subscribe(@Valid @PathVariable UUID chatId) {
        return chatService.subscribeOnChat(chatId);
    }
}
