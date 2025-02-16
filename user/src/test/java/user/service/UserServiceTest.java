package user.service;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import user.dto.request.CreateUserAccountRequest;
import user.dto.request.UpdateUserInfoRequest;
import user.entity.Role;
import user.entity.User;
import user.exception.InternalException;
import user.repository.UserRepository;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    //@Mock
    //private UsersChatsRepository usersChatsRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void createAccountTest_ok() {
        CreateUserAccountRequest request = new CreateUserAccountRequest(
            "name",
            "surname",
            "email",
            "password",
            "briefDescription",
            "City",
            LocalDate.now(),
            "logo_url",
            Role.SUPERVISOR
        );
        User user = createUser();
        when(userRepository.findByEmail(any())).thenReturn(Mono.empty());
        when(userRepository.save(any())).thenReturn(Mono.just(user));

        StepVerifier.create(userService.createAccount(request))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    public void createAccountTest_exists() {
        CreateUserAccountRequest request = new CreateUserAccountRequest(
            "name",
            "surname",
            "email",
            "password",
            "briefDescription",
            "City",
            LocalDate.now(),
            "logo_url",
            Role.SUPERVISOR
        );
        User user = createUser();
        when(userRepository.findByEmail(any())).thenReturn(Mono.just(user));
        when(userRepository.save(any())).thenReturn(Mono.just(user));

        StepVerifier.create(userService.createAccount(request))
            .expectError(InternalException.class)
            .verify();
    }

    @Test
    public void updateAccountTest_ok() {
        User user = createUser();
        UpdateUserInfoRequest request = new UpdateUserInfoRequest(
            user.getId(),
            user.getName(),
            user.getSurname(),
            user.getEmail(),
            user.getBriefDescription(),
            user.getCity(),
            user.getBirthday(),
            user.getLogoUrl()
        );

        when(userRepository.findById(user.getId())).thenReturn(Mono.just(user));
        when(userRepository.save(any())).thenReturn(Mono.just(user));

        StepVerifier.create(userService.updateAccount(request))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    public void updateAccountTest_not_found() {
        User user = createUser();
        UpdateUserInfoRequest request = new UpdateUserInfoRequest(
            user.getId(),
            user.getName(),
            user.getSurname(),
            user.getEmail(),
            user.getBriefDescription(),
            user.getCity(),
            user.getBirthday(),
            user.getLogoUrl()
        );

        when(userRepository.findById(user.getId())).thenReturn(Mono.empty());

        StepVerifier.create(userService.updateAccount(request))
            .expectError(InternalException.class)
            .verify();
    }

    @Test
    public void deleteAccount_not_found() {
        User user = createUser();

        when(userRepository.findById(user.getId())).thenReturn(Mono.empty());

        StepVerifier.create(userService.deleteAccountById(user.getId()))
            .expectError(InternalException.class)
            .verify();
    }

    @Test
    public void deleteAccount_ok() {
        User user = createUser();

        when(userRepository.findById(user.getId())).thenReturn(Mono.just(user));
        when(userRepository.deleteById(user.getId())).thenReturn(Mono.empty());
        StepVerifier.create(userService.deleteAccountById(user.getId()))
            .expectNextCount(0)
            .verifyComplete();
    }

    @Test
    public void getAccountById_ok() {
        User user = createUser();
        when(userRepository.findById(user.getId())).thenReturn(Mono.just(user));

        StepVerifier.create(userService.getAccountById(user.getId()))
            .expectNextCount(1)
            .verifyComplete();
    }

    private User createUser() {
        return new User(UUID.randomUUID(), "name", "surname", "email", "password", "briefDescription", "City", LocalDate.now(), "logo_url", "SUPERVISOR");
    }
}
