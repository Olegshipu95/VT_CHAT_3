package messenger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messenger.dto.chat.request.CreateChatRequest;
import messenger.dto.chat.response.ChatForResponse;
import messenger.dto.chat.response.MessageForResponse;
import messenger.dto.chat.response.ResponseSearchChat;
import messenger.dto.chat.response.ResponseSearchMessage;
import messenger.entity.Chat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "Создание чата")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Чат создан"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @PostMapping
    public ResponseEntity<?> createChat(@Valid @RequestBody CreateChatRequest createChatRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createChat(createChatRequest));
    }

    @Operation(summary = "Поиск чата")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Чат найден",
            content = @Content(schema = @Schema(implementation = Chat.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Чат не найден",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getChat(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.findById(id));
    }

    @Operation(summary = "Удаление чата")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Чат удален"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> deleteChat(@NotNull(message = ErrorMessages.ID_CANNOT_BE_NULL) @PathVariable UUID chatId) {
        chatService.deleteChat(chatId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Добавление пользователя в чат")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Добавлен"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @PostMapping("/users")
    public ResponseEntity<?> addUserChats(@RequestBody UsersChatsRequest usersChats) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.addUserChats(usersChats));
    }

    @Operation(summary = "Поиск чатов пользователя")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Найдены",
            content = @Content(schema = @Schema(implementation = ChatForResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @GetMapping("/users")
    public ResponseEntity<?> getUsersChats(
        @Parameter(name = "request") @RequestParam(required = false) String request,
        @Schema(hidden = true) @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(chatService.getUsersChats(UUID.fromString(SecurityContextHolder.getUserId()), request, (long) pageable.getPageNumber(), (long) pageable.getPageSize()));
    }

    @Operation(summary = "Отправка сообщения в чат")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Отправлено"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @PostMapping("/{id}/send")
    public ResponseEntity<?> sendMessage(
        @PathVariable UUID id,
        @Valid @RequestBody SendMessage message
    ) {
        UUID response = chatService.sendMessage(id, message);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Получение сообщения из чата")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Получено",
            content = @Content(schema = @Schema(implementation = MessageForResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @GetMapping("/{id}/messages")
    public ResponseEntity<?> getMessage(
        @PathVariable UUID id,
        @Parameter(name = "request") @RequestParam(required = false) String request,
        @Schema(hidden = true) @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(chatService.searchMessage(id, request, pageable));
    }

    @Operation(summary = "Подписка на чат")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Получено",
            content = @Content(schema = @Schema(implementation = MessageForResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @GetMapping(value = "/subscribe/{chatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<MessageForResponse> subscribe(@Valid @PathVariable UUID chatId) {
        return chatService.subscribeOnChat(chatId);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Сообщение для отправки")
    public static class SendMessage {
        @Schema(description = "Текст")
        private String text;
        private List<String> photos;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "добавление пользователя в чат")
    public static class UsersChatsRequest {
        @Schema(description = "Id пользователя")
        private UUID userId;
        @Schema(description = "Id чата")
        private UUID chatId;
    }
}
