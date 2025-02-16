package user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import user.dto.request.CreateUserAccountRequest;
import user.dto.request.UpdateUserInfoRequest;
import user.dto.response.GetUserInfoResponse;
import user.entity.User;
import user.exception.ErrorCode;
import user.exception.InternalException;
import user.repository.UserRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UsersChatsServiceClient usersChatsService;

    public Mono<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public Mono<UUID> createAccount(CreateUserAccountRequest request) {
        log.debug("Create an account for: {}", request.name());
        User newUser = new User(
            null,
            request.name(),
            request.surname(),
            request.email(),
            passwordEncoder.encode(request.password()),
            request.briefDescription(),
            request.city(),
            request.birthday(),
            request.logoUrl(),
            request.role().name()
        );
        return userRepository.findByEmail(newUser.getEmail())
            .doOnNext(user -> {
                throw new InternalException(HttpStatus.BAD_REQUEST, ErrorCode.USER_ALREADY_EXIST);
            })
            .switchIfEmpty(userRepository.save(newUser))
            .map(User::getId)
            /*.flatMap(id ->
                UsersChats usersChats = new UsersChats();
                usersChats.setId(UUID.randomUUID());
                usersChats.setUserId(id);
                usersChats.setChats(new ArrayList<>());
                return Mono.just(usersChatsService.save(usersChats))
                    .onErrorResume(ex -> {
                        log.warn("Ошибка при вызове feign-клиента: {}", ex.getMessage());
                        return Mono.empty(); // Игнорируем ошибку и продолжаем выполнение
                    })
                    .thenReturn(id);
            )*/
            .doOnSuccess(id -> log.info("User with ID: {} has been successfully created.", id));
    }

    public Mono<UUID> updateAccount(UpdateUserInfoRequest request) {
        log.debug("UPDATE: start for id: {}", request.userId());
        return userRepository.findById(request.userId())  // 1. Ищем пользователя
            .switchIfEmpty(Mono.error(new InternalException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND)))  // 2. Если не нашли, выбрасываем ошибку
            .flatMap(existingUser -> {
                // 3. Обновляем только непустые поля
                if (request.name() != null) {
                    existingUser.setName(request.name());
                }
                if (request.surname() != null) {
                    existingUser.setSurname(request.surname());
                }
                if (request.email() != null) {
                    existingUser.setEmail(request.email());
                }
                if (request.briefDescription() != null) {
                    existingUser.setBriefDescription(request.briefDescription());
                }
                if (request.city() != null) {
                    existingUser.setCity(request.city());
                }
                if (request.birthday() != null) {
                    existingUser.setBirthday(request.birthday());
                }
                if (request.logoUrl() != null) {
                    existingUser.setLogoUrl(request.logoUrl());
                }
                return userRepository.save(existingUser);  // 4. Сохраняем обновлённого пользователя
            })
            .map(User::getId)  // 5. Возвращаем обновлённый ID
            .doOnSuccess(id -> log.info("User with ID: {} has been successfully updated.", id));
    }


    public Mono<GetUserInfoResponse> getAccountById(UUID id) {
        log.debug("GET: start for id: {}", id);
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(new InternalException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND)))
            .map(account -> new GetUserInfoResponse(
                account.getId(),
                account.getName(),
                account.getSurname(),
                account.getEmail(),
                account.getBriefDescription(),
                account.getCity(),
                account.getBirthday(),
                account.getLogoUrl()
            )).doOnSuccess(response -> log.info("GET: ID: {} has been successfully retrieved.", id));
    }

    public Mono<Void> deleteAccountById(UUID id) {
        log.debug("DELETE: start for id: {}", id);
        return userRepository.findById(id)
            .switchIfEmpty(Mono.error(new InternalException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND)))
            .flatMap(account -> userRepository.deleteById(id))
            .doOnSuccess(voidValue -> log.info("DELETE: ID: {} has been successfully deleted.", id));
    }
}
