package user.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import user.dto.request.AuthorizeUserRequest;
import user.dto.response.AuthorizationDetails;
import user.dto.response.AuthorizeUserResponse;
import user.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Hidden
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public Mono<AuthorizationDetails> getAuthDetails() {
        return authService.getAuthorizationDetails();
    }

    @PostMapping("/tokens")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthorizeUserResponse> authorize(@Valid @RequestBody Mono<AuthorizeUserRequest> request) {
        return request.flatMap(req -> authService.authorize(req.username(), req.password()));
    }

    @PostMapping("/tokens/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AuthorizeUserResponse> reAuthorize(@RequestParam String refresh) {
        return authService.reAuthorize(refresh);
    }
}
