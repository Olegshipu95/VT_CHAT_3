package subscription.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import subscription.dto.subs.request.CreateSubRequest;
import subscription.dto.subs.response.SubscriptionResponse;
import subscription.entity.Subscribers;
import subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/subscribe")
public class SubscriptionController {

    private final SubscriptionService service;

    @Operation(summary = "Подписаться")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Подписано",
            content = @Content(schema = @Schema(implementation = CreateSubRequest.class))
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
    public ResponseEntity<UUID> makeSubscription(@RequestBody @Valid CreateSubRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSub(request));
    }

    @Operation(summary = "Поиск подписки по id")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Найдено",
            content = @Content(schema = @Schema(implementation = Subscribers.class))
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
            description = "Не найдено",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @GetMapping("/{subId}")
    public ResponseEntity<Subscribers> getSubscriptionById(@PathVariable UUID subId) {
        Subscribers subscription = service.getSub(subId);
        return ResponseEntity.ok(subscription);
    }

    @Operation(summary = "Удаление подписки по id")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Удалено"
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
            description = "Не найдено",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @DeleteMapping("/{subId}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable UUID subId) {
        service.deleteSub(subId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Operation(summary = "Поиск подписок пользователя по id")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Найдено",
            content = @Content(schema = @Schema(implementation = SubscriptionResponse.class))
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
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionResponse>> getSubscriptionsByUserId(@PathVariable UUID userId) {
        List<SubscriptionResponse> subscriptions = service.getSubscriptionsByUserId(userId);
        return ResponseEntity.ok(subscriptions);
    }
}
