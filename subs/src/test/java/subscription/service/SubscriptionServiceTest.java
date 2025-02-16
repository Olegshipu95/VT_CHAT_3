package subscription.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subscription.dto.User;
import subscription.dto.subs.request.CreateSubRequest;
import subscription.dto.subs.response.SubscriptionResponse;
import subscription.entity.Subscribers;
import subscription.exception.InternalException;
import subscription.repository.SubRepository;
import subscription.utils.SecurityMock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private SubRepository subscribersRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @BeforeEach
    public void setUp() {
        SecurityMock.mockSecurityContext();
    }

    @Test
    public void createSub_OK() {
        CreateSubRequest request = new CreateSubRequest(UUID.randomUUID());
        Subscribers subscribers = new Subscribers(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now());
        when(subscribersRepository.existsByUserIdAndSubscribedUserId(any(), any())).thenReturn(false);
        when(userService.findById(any())).thenReturn(new User());
        when(subscribersRepository.save(any())).thenReturn(subscribers);

        UUID id = subscriptionService.createSub(request);

        assertNotNull(id);
        verify(subscribersRepository, times(1)).existsByUserIdAndSubscribedUserId(any(), any());
    }

    @Test
    public void createSub_USER_NOT_FOUND() {
        CreateSubRequest request = new CreateSubRequest(UUID.randomUUID());
        when(userService.findById(any())).thenReturn(null);

        assertThrows(InternalException.class, () -> subscriptionService.createSub(request));
    }

    @Test
    public void getSub_USER_NOT_FOUND() {
        when(subscribersRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(InternalException.class, () -> subscriptionService.getSub(UUID.randomUUID()));
    }

    @Test
    public void getSub_OK() {
        Subscribers subscribers = new Subscribers(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now());

        when(subscribersRepository.findById(any())).thenReturn(Optional.of(subscribers));

        Subscribers subs =  subscriptionService.getSub(UUID.randomUUID());

        assertEquals(subs, subscribers);
    }

    @Test
    public void deleteSub_ok() {
        UUID id = UUID.randomUUID();
        when(subscribersRepository.existsById(any())).thenReturn(true);

        assertDoesNotThrow(() -> subscriptionService.deleteSub(id));
    }

    @Test
    public void deleteSub_not_found() {
        UUID id = UUID.randomUUID();
        when(subscribersRepository.existsById(any())).thenReturn(false);

        assertThrows(InternalException.class, () -> subscriptionService.deleteSub(id));
    }

    @Test
    public void getSubscriptionsByUserId_ok() {
        SubscriptionResponse subscriptionResponse = new SubscriptionResponse(UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now());
        when(subscribersRepository.getSubResponseByUserId(any())).thenReturn(List.of(subscriptionResponse));

        List<SubscriptionResponse> expected = subscriptionService.getSubscriptionsByUserId(UUID.randomUUID());
        assertEquals(expected.stream().findFirst().get(), subscriptionResponse);
    }
}