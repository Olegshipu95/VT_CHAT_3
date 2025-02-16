package user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import user.config.security.AuthenticationProvider;
import user.dto.response.AuthorizationDetails;
import user.dto.response.AuthorizeUserResponse;
import user.entity.Role;
import user.entity.User;
import user.exception.ErrorCode;
import user.exception.InternalException;
import user.repository.UserRepository;
import user.utils.SecurityContext;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthenticationProvider authProvider;

    public Mono<AuthorizationDetails> getAuthorizationDetails() {
        return SecurityContext.getAuthorizationDetails();
    }

    public Mono<AuthorizeUserResponse> authorize(String email, String password) {
        return userRepository.findByEmail(email)
            .switchIfEmpty(Mono.error(new InternalException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND)))
            .flatMap(user -> {
            String encodedAuthUserPassword = encoder.encode(password);
            if (encoder.matches(user.getPassword(), encodedAuthUserPassword)) {
                return Mono.error(new InternalException(HttpStatus.UNAUTHORIZED, ErrorCode.USER_PASSWORD_INCORRECT));
            }
            return createAuthorizeUserResponse(user);
        });
    }

    public Mono<AuthorizeUserResponse> reAuthorize(String refreshToken) {
        if (!authProvider.isValid(refreshToken)) {
            return Mono.error(new InternalException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_EXPIRED));
        }
        UUID id = UUID.fromString(authProvider.getIdFromToken(refreshToken));
        return userRepository.findById(id)
            .flatMap(this::createAuthorizeUserResponse);
    }

    private Mono<AuthorizeUserResponse> createAuthorizeUserResponse(User user) {
        return Mono.just(new AuthorizeUserResponse(
            user.getId(),
            user.getEmail(),
            authProvider.createAccessToken(user.getId(), user.getEmail(), Set.of(Role.valueOf(user.getRole()))),
            authProvider.createRefreshToken(user.getId(), user.getEmail())
        ));
    }
}
