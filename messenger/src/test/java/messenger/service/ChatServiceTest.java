package messenger.service;

import messenger.dto.chat.request.CreateChatRequest;
import messenger.dto.chat.response.ResponseGettingChats;
import messenger.dto.chat.response.ResponseGettingMessages;
import messenger.dto.chat.response.ResponseSearchChat;
import messenger.dto.chat.response.ResponseSearchMessage;
import messenger.entity.Chat;
import messenger.entity.Message;
import messenger.entity.UsersChats;
import messenger.exception.InternalException;
import messenger.repository.chat.ChatRepository;
import messenger.utils.SecurityMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class ChatServiceTest {


    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UsersChatsService usersChatsService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    public void setUp() {
        SecurityMock.mockSecurityContext();
    }

    @Test
    public void findById_not_found() {

        when(chatRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(InternalException.class, () -> chatService.findById(UUID.randomUUID()));
    }

    @Test
    public void findById_OK() {
        Chat chat = new Chat(UUID.randomUUID(), "name", 0);
        when(chatRepository.findById(any())).thenReturn(Optional.of(chat));

        Chat expected =  chatService.findById(UUID.randomUUID());

        assertEquals(expected, chat);
    }

    @Test
    public void createChat_ok() {
        Chat chat = new Chat(UUID.randomUUID(), "name", 0);
        CreateChatRequest createChatRequest = new CreateChatRequest(0, "name", List.of(UUID.randomUUID(), UUID.randomUUID()));
        when(chatRepository.save(any())).thenReturn(chat);
        UsersChats usersChats = new UsersChats(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        when(usersChatsService.findByUserId(any())).thenReturn(Optional.of(usersChats));

        UUID id = chatService.createChat(createChatRequest);

        assertNotNull(id);
        verify(chatRepository, times(1)).save(any());
        verify(usersChatsService, times(2)).findByUserId(any());
    }

    @Test
    public void createChat_BAD_REQUEST() {
        Chat chat = new Chat(UUID.randomUUID(), "name", 1);
        CreateChatRequest createChatRequest = new CreateChatRequest(0, "name", List.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()));
        when(chatRepository.save(any())).thenReturn(chat);

        assertThrows(InternalException.class, () -> chatService.createChat(createChatRequest));

        verify(chatRepository, times(1)).save(any());
    }

    @Test
    public void sendMessage_ok() {
        Chat chat = new Chat(UUID.randomUUID(), "name", 0);
        Message message = new Message(UUID.randomUUID(), chat, UUID.randomUUID(), "text", Timestamp.valueOf(LocalDateTime.now()), List.of());

        when(chatRepository.findById(any())).thenReturn(Optional.of(chat));
        UUID id = chatService.sendMessage(UUID.randomUUID(), message);

        assertNotNull(id);
        verify(messageService, times(1)).save(any());
    }

    @Test
    public void subscribeOnChat() {

        assertDoesNotThrow(() -> chatService.subscribeOnChat(UUID.randomUUID()));
    }

    @Test
    public void getUsersChats_ok() {
        Chat chat = new Chat(UUID.randomUUID(), "name", 0);
        Message message = new Message(UUID.randomUUID(), chat, UUID.randomUUID(), "text", Timestamp.valueOf(LocalDateTime.now()), List.of());
        UsersChats usersChats = new UsersChats(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        when(usersChatsService.findByUserId(any())).thenReturn(Optional.of(usersChats));
        when(chatRepository.findByNameContainingAndIdIn(any(),any(), any())).thenReturn(new PageImpl<>(List.of(chat)));
        when(messageService.findLastByChatId(any())).thenReturn(Optional.of(message));
        when(chatRepository.findById(any())).thenReturn(Optional.of(chat));
        when(usersChatsService.findIdsByChatId(any())).thenReturn(List.of(UUID.randomUUID()));

        ResponseSearchChat response = chatService.getUsersChats(UUID.randomUUID(), "request", 0L, 10L);

        assertNotNull(response);
        verify(usersChatsService, times(1)).findByUserId(any());
        verify(chatRepository, times(1)).findByNameContainingAndIdIn(any(), any(), any());
    }

    @Test
    public void searchMessage_ok() {
        Chat chat = new Chat(UUID.randomUUID(), "name", 0);
        Message message = new Message(UUID.randomUUID(), chat, UUID.randomUUID(), "text", Timestamp.valueOf(LocalDateTime.now()), List.of());
        when(messageService.findByTextContainingAndChatId(any(), any(), any())).thenReturn(new PageImpl<>(List.of(message)));

        ResponseSearchMessage response = chatService.searchMessage(UUID.randomUUID(), "text", Pageable.ofSize(10));

        assertNotNull(response);
        verify(messageService, times(1)).findByTextContainingAndChatId(any(), any(), any());
    }

    @Test
    public void deleteChat_ok() {
        when(usersChatsService.findIdsByChatId(any())).thenReturn(List.of(UUID.randomUUID(), UUID.randomUUID()));
        when(usersChatsService.findById(any())).thenReturn(Optional.of(new UsersChats(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>())));

        assertDoesNotThrow(() -> chatService.deleteChat(UUID.randomUUID()));
    }

    @Test
    public void getAllChatsByUserId() {
        Chat chat = new Chat(UUID.randomUUID(), "name", 0);
        Message message = new Message(UUID.randomUUID(), chat, UUID.randomUUID(), "text", Timestamp.valueOf(LocalDateTime.now()), List.of());

        when(usersChatsService.findByUserId(any())).thenReturn(Optional.of(new UsersChats(UUID.randomUUID(), UUID.randomUUID(), List.of(UUID.randomUUID(), UUID.randomUUID()))));
        when(messageService.findLastByChatId(any())).thenReturn(Optional.of(message));
        when(chatRepository.findById(any())).thenReturn(Optional.of(chat));
        when(usersChatsService.findIdsByChatId(any())).thenReturn(List.of(UUID.randomUUID(), UUID.randomUUID()));

        ResponseGettingChats response = chatService.getAllChatsByUserId(UUID.randomUUID(), 0L, 10L);
        assertNotNull(response);
        verify(chatRepository, times(2)).findById(any());
        verify(messageService, times(2)).findLastByChatId(any());
    }

    @Test
    public void getAllMessagesByChatId() {
        Chat chat = new Chat(UUID.randomUUID(), "name", 0);
        Message message = new Message(UUID.randomUUID(), chat, UUID.randomUUID(), "text", Timestamp.valueOf(LocalDateTime.now()), List.of());

        when(messageService.findByChatId(any(), any())).thenReturn(new PageImpl<>(List.of(message)));

        ResponseGettingMessages response = chatService.getAllMessagesByChatId(UUID.randomUUID(), 0L, 10L);

        assertNotNull(response);
        verify(messageService, times(1)).findByChatId(any(), any());
    }

    @Test
    public void addUserChats_ok() {
        UsersChats usersChats = new UsersChats(UUID.randomUUID(), UUID.randomUUID(), new ArrayList<>());
        when(usersChatsService.save(any())).thenReturn(usersChats);

        UsersChats expected = chatService.addUserChats(usersChats);

        assertNotNull(expected);
        assertEquals(usersChats, expected);
        verify(usersChatsService, times(1)).save(any());
    }
}
