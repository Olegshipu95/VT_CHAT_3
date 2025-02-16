package subscription.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import subscription.dto.EmailNotification;
import subscription.dto.User;
import subscription.dto.subs.request.CreateSubRequest;
import subscription.dto.subs.response.SubscriptionResponse;
import subscription.entity.Subscribers;
import subscription.exception.ErrorCode;
import subscription.exception.InternalException;
import subscription.kafka.KafkaNotificationProducer;
import subscription.repository.SubRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscription.utils.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final UserService userService;

    private final SubRepository subscribersRepository;

    private final KafkaNotificationProducer kafkaNotificationProducer;

    @Transactional
    public UUID createSub(CreateSubRequest sub) {
        String userId = SecurityContextHolder.getUserId();
        log.debug("CREATE: start addSubscriber with userId: {} subscribedUserId: {}", userId, sub.getSubscribedUserId());
        User user = validateUsersExistAndGet(sub.getSubscribedUserId());
        UUID newId = UUID.randomUUID();
        Subscribers subscriber = new Subscribers();
        subscriber.setId(newId);
        subscriber.setUserId(UUID.fromString(userId));
        subscriber.setSubscribedUserId(sub.getSubscribedUserId());
        subscriber.setSubscriptionTime(LocalDateTime.now());
        if (subscribersRepository.existsByUserIdAndSubscribedUserId(UUID.fromString(userId), sub.getSubscribedUserId())) {
            throw new InternalException(HttpStatus.BAD_REQUEST, ErrorCode.SUBSCRIPTION_ALREADY_EXISTS);
        }
        var newSub = subscribersRepository.save(subscriber);
        kafkaNotificationProducer.sendEmailNotification(EmailNotification.builder()
            .email(user.getEmail())
            .title("У вас новый подписчик!")
            .text("Поздравляем на вас подписался товарищ: " + SecurityContextHolder.getUsername())
            .build());
        log.info("CREATE: new sub {}", newSub.getId());
        return newSub.getId();
    }

    public Subscribers getSub(UUID subId) {
        log.debug("GET: subscriber with ID: {}", subId);
        return subscribersRepository.findById(subId)
                .orElseThrow(() -> new InternalException(HttpStatus.NOT_FOUND, ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }

    @Transactional
    public void deleteSub(UUID subId) {
        log.debug("DELETE: subscriber with ID: {}", subId);
        if (!subscribersRepository.existsById(subId)) {
            log.info("Subscription with ID {} does not exist", subId);
            throw new InternalException(HttpStatus.NOT_FOUND, ErrorCode.SUBSCRIPTION_NOT_FOUND);
        }
        subscribersRepository.deleteById(subId);
    }

    public List<SubscriptionResponse> getSubscriptionsByUserId(UUID userId) {
        log.info("GET: subscriptions for userId: {}", userId);
        return subscribersRepository.getSubResponseByUserId(userId);
    }

    private User validateUsersExistAndGet(UUID subscribedUserId) {
        try {
            User user = userService.findById(subscribedUserId);
            if (user == null) {
                log.info("Subscribed User ID {} does not exist", subscribedUserId);
                throw new InternalException(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND);
            }
            log.info("User email - {}", user.getEmail());
            return user;
        } catch (FeignException e){
            log.warn("Ошибка при вызове Feign-клиента: {}", e.getMessage());
            throw new InternalException(HttpStatus.BAD_REQUEST, ErrorCode.USER_NOT_FOUND);
        }
    }
}