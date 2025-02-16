package user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import user.config.security.AuthenticationProvider;
import user.entity.User;
import user.exception.InternalException;
import user.repository.UserRepository;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    AuthenticationProvider authenticationProvider;

    @InjectMocks
    AuthService authService;

    @Test
    public void authorizeTest_OK() {
        User user = createUser();
        when(userRepository.findByEmail(any())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        when(authenticationProvider.createAccessToken(any(), any(), any())).thenReturn("access");
        when(authenticationProvider.createRefreshToken(any(), any())).thenReturn("refresh");

        StepVerifier.create(authService.authorize(user.getEmail(), user.getPassword()))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    public void authorizeTest_NOT_FOUND() {
        when(userRepository.findByEmail(any())).thenReturn(Mono.empty());

        StepVerifier.create(authService.authorize("email", "password"))
            .expectError(InternalException.class)
            .verify();
    }

    @Test
    public void authorizeTest_WROND_PASSWORD() {
        User user = createUser();
        when(userRepository.findByEmail(any())).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        StepVerifier.create(authService.authorize(user.getEmail(), user.getPassword()))
            .expectError(InternalException.class)
            .verify();
    }

    @Test
    public void reAuthorizeTest_OK() {
        User user = createUser();
        when(authenticationProvider.isValid(any())).thenReturn(true);
        when(authenticationProvider.getIdFromToken(any())).thenReturn(user.getId().toString());
        when(userRepository.findById(user.getId())).thenReturn(Mono.just(user));
        when(authenticationProvider.createAccessToken(any(), any(), any())).thenReturn("access");
        when(authenticationProvider.createRefreshToken(any(), any())).thenReturn("refresh");

        StepVerifier.create(authService.reAuthorize("Token"))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    public void reAuthorizeTest_BAD_TOKEN() {
        when(authenticationProvider.isValid(any())).thenReturn(false);

        StepVerifier.create(authService.reAuthorize("Token"))
            .expectError(InternalException.class)
            .verify();
    }

    private User createUser() {
        return new User(UUID.randomUUID(), "name", "suranem", "email", "passwird", "briefDescription", "City", LocalDate.now(), "logo_url", "SUPERVISOR");
    }
}
