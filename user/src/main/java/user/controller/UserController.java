package user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import user.dto.request.CreateUserAccountRequest;
import user.dto.request.UpdateUserInfoRequest;
import user.dto.response.AuthorizeUserResponse;
import user.dto.response.GetUserInfoResponse;
import user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Создание пользователя")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Создано"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Не авторизован",
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
    @PreAuthorize("hasAnyAuthority('SUPERVISOR')")
    public Mono<ResponseEntity<UUID>> createAccount(@Valid @RequestBody CreateUserAccountRequest createUserAccountRequest) {
        return userService.createAccount(createUserAccountRequest)
                .map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id));
    }

    @Operation(summary = "Обновление пользователя")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Обновлен"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Не авторизован",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Не найден",
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
    @PutMapping
    public Mono<ResponseEntity<UUID>> updateAccount(@RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
        return userService.updateAccount(updateUserInfoRequest)
                .map(id -> ResponseEntity.status(HttpStatus.OK).body(id));
    }

    @Operation(summary = "Поиск пользователя")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Найден",
            content = @Content(schema = @Schema(implementation = GetUserInfoResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Не авторизован",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Не найден",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @GetMapping("/{id}")
    public Mono<ResponseEntity<GetUserInfoResponse>> getAccountById(@PathVariable(value = "id") UUID id) {
        return userService.getAccountById(id)
                .map(response -> ResponseEntity.status(HttpStatus.OK).body(response));
    }


    @Operation(summary = "Удаление пользователя")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Найден"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверный запрос",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Не авторизован",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Недоступно",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Не найден",
            content = @Content(schema = @Schema(implementation = Error.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Сервис не доступен",
            content = @Content(schema = @Schema(implementation = Error.class))
        )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR')")
    public Mono<ResponseEntity<Void>> deleteAccountById(@PathVariable(value = "id") UUID id) {
        return userService.deleteAccountById(id)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
    }
}