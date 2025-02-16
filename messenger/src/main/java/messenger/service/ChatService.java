package messenger.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messenger.controller.ChatController;
import messenger.dto.chat.request.CreateChatRequest;
import messenger.dto.chat.response.ChatForResponse;
import messenger.dto.chat.response.MessageForResponse;
import messenger.dto.chat.response.ResponseGettingChats;
import messenger.dto.chat.response.ResponseGettingMessages;
import messenger.dto.chat.response.ResponseSearchChat;
import messenger.dto.chat.response.ResponseSearchMessage;
import messenger.entity.Chat;
import messenger.entity.ChatType;
import messenger.entity.Message;
import messenger.entity.UsersChats;
import messenger.exception.ErrorCode;
import messenger.exception.InternalException;
import messenger.repository.chat.ChatRepository;
import messenger.repository.chat.UsersChatsRepository;
import messenger.utils.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    private final UsersChatsService usersChatsService;

    private final MessageService messageService;

    private final UsersChatsRepository usersChatsRepository;

    private final ConcurrentHashMap<UUID, List<MessageForResponse>> chatMessages = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<UUID, ConcurrentLinkedQueue<DeferredResult<MessageForResponse>>> chatClients = new ConcurrentHashMap<>();

    public Chat findById(UUID id) {
        return chatRepository.findById(id)
            .orElseThrow(() -> new InternalException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND));
    }

    @Transactional
    public UUID createChat(CreateChatRequest createChatRequest) {
        Chat chatForSave = new Chat();
        chatForSave.setId(UUID.randomUUID());
        chatForSave.setChatType(createChatRequest.getChatType());
        chatForSave.setName(createChatRequest.getName());
        Chat savedChat = chatRepository.save(chatForSave);
        List<UUID> listUsersIds = new ArrayList<>();
        if (chatForSave.getChatType() == 0 && listUsersIds.size() > 2) {
            throw new InternalException(HttpStatus.BAD_REQUEST, ErrorCode.USER_COUNT_ERROR);
        }
        Set<UUID> uniqueIds = new HashSet<>();
        if (!listUsersIds.stream().allMatch(uniqueIds::add)) {
            throw new InternalException(HttpStatus.BAD_REQUEST, ErrorCode.USER_DUPLICATED);
        }
        log.info("Chat with ID: {} has been successfully created.", savedChat.getId());
        return savedChat.getId();
    }

    @Transactional
    public UUID sendMessage(UUID chatId, ChatController.SendMessage newMessage) {
        Message message = new Message();
        message.setChatId(chatRepository.findById(chatId).orElseThrow(() -> new InternalException(HttpStatus.NOT_FOUND, ErrorCode.CHAT_NOT_FOUND)));
        message.setId(UUID.randomUUID());
        message.setAuthorId(UUID.fromString(SecurityContextHolder.getUserId()));
        message.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        message.setText(newMessage.getText());
        message.setPhotos(newMessage.getPhotos());
        messageService.save(message);
        MessageForResponse messageForResponse = new MessageForResponse(message);
        List<MessageForResponse> messages = chatMessages.computeIfAbsent(chatId, k -> new ArrayList<>());
        messages.add(messageForResponse);
        ConcurrentLinkedQueue<DeferredResult<MessageForResponse>> clients = chatClients.get(chatId);
        if (clients != null) {
            clients.forEach(client -> client.setResult(messageForResponse));
            clients.clear();
        }
        return message.getId();
    }

    @Transactional
    public DeferredResult<MessageForResponse> subscribeOnChat(UUID chatId) {
        DeferredResult<MessageForResponse> result = new DeferredResult<>(60000L);
        chatClients.computeIfAbsent(chatId, k -> new ConcurrentLinkedQueue<>()).add(result);
        result.onCompletion(() -> {
            ConcurrentLinkedQueue<DeferredResult<MessageForResponse>> clients = chatClients.get(chatId);
            if (clients != null) {
                clients.remove(result);
            }
        });
        return result;
    }


    @Transactional
    public ResponseSearchChat getUsersChats(UUID userId, String request, Long pageNumber, Long countChatsOnPage) {
        if (request == null) request = "";
        List<UUID> userChats = usersChatsService.findByUserId(userId).stream()
            .map(UsersChats::getChats)
            .flatMap(List::stream)
            .toList();
        List<Chat> listOfChats = chatRepository.findByNameContainingAndIdIn(userChats, request, PageRequest.of(pageNumber.intValue(), countChatsOnPage.intValue()))
            .stream()
            .toList();
        ResponseSearchChat responseSearchChat = new ResponseSearchChat();
        responseSearchChat.setResponse(new ArrayList<>());
        for (int i = 0; i < listOfChats.size(); i++) {
            ChatForResponse chatForResponse = new ChatForResponse();
            Optional<Message> lastMessageOpt = messageService.findLastByChatId(listOfChats.get(i).getId());
            if (lastMessageOpt.isPresent()) {
                Message lastMessage = lastMessageOpt.get();
                chatForResponse.setLastMessage(lastMessage.getText());
                chatForResponse.setLastMessageHavePhoto(false);
            } else {
                chatForResponse.setLastMessage("");
                chatForResponse.setLastMessageHavePhoto(false);
            }
            Chat chat = chatRepository.findById(listOfChats.get(i).getId()).orElseThrow(() -> new InternalException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND));
            chatForResponse.setId(chat.getId());
            chatForResponse.setChatType(ChatType.values()[chat.getChatType()]);
            chatForResponse.setCountMembers(usersChatsService.findIdsByChatId(listOfChats.get(i).getId()).size());
            responseSearchChat.response.add(i, chatForResponse);
        }
        return responseSearchChat;
    }

    @Transactional
    public ResponseSearchMessage searchMessage(UUID chatId, String request, Pageable pageRequest) {
        if (request == null) request = "";
        return new ResponseSearchMessage(messageService.findByTextContainingAndChatId(chatId, request, PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize()))
            .stream()
            .map(MessageForResponse::new)
            .collect(Collectors.toList()));
    }

    @Transactional
    public void deleteChat(UUID chatId) {
        List<UUID> usersChatsIds = usersChatsService.findIdsByChatId(chatId);
        for (UUID usersChatsId : usersChatsIds) {
            UsersChats usersChats = usersChatsService.findById(usersChatsId).orElseThrow(() -> new InternalException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND));
            usersChats.getChats().remove(chatId);
            usersChatsService.save(usersChats);
        }
        chatRepository.deleteById(chatId);
    }

    @Transactional
    public ResponseGettingChats getAllChatsByUserId(UUID userId, Long pageNumber, Long countChatsOnPage) {
        List<UUID> usersChats = usersChatsService.findByUserId(userId).stream()
            .map(UsersChats::getChats)
            .flatMap(List::stream)
            .toList();
        int startIndex = (int) (pageNumber * countChatsOnPage);
        int toIndex = (int) (startIndex + countChatsOnPage);
        if (usersChats.size() < toIndex) {
            toIndex = usersChats.size();
        }
        List<UUID> userChats = usersChats.subList(startIndex, toIndex);
        ResponseGettingChats responseGettingChats = new ResponseGettingChats();
        responseGettingChats.setResponse(new ArrayList<>());
        for (int i = 0; i < userChats.size(); i++) {
            ChatForResponse chatForResponse = new ChatForResponse();
            Optional<Message> lastMessageOpt = messageService.findLastByChatId(userChats.get(i));
            if (lastMessageOpt.isPresent()) {
                Message lastMessage = lastMessageOpt.get();
                chatForResponse.setLastMessage(lastMessage.getText());
                chatForResponse.setLastMessageHavePhoto(false);
            } else {
                chatForResponse.setLastMessage("");
                chatForResponse.setLastMessageHavePhoto(false);
            }
            Chat chat = chatRepository.findById(userChats.get(i)).orElseThrow(() -> new InternalException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND));
            chatForResponse.setId(chat.getId());
            chatForResponse.setChatType(ChatType.values()[chat.getChatType()]);
            chatForResponse.setCountMembers(usersChatsService.findIdsByChatId(userChats.get(i)).size());
            responseGettingChats.response.add(i, chatForResponse);
        }
        return responseGettingChats;
    }

    @Transactional
    public ResponseGettingMessages getAllMessagesByChatId(UUID chatId, Long pageNumber, Long countMessagesOnPage) {
        Page<Message> pageOfMessages = messageService.findByChatId(chatId, PageRequest.of(pageNumber.intValue(), countMessagesOnPage.intValue()));
        return new ResponseGettingMessages(
            pageOfMessages.stream()
                .map(MessageForResponse::new)
                .collect(Collectors.toList())
        );
    }

    public UsersChats addUserChats(ChatController.UsersChatsRequest usersChats) {
        Optional<UsersChats> pair = usersChatsRepository.findByUserIdAndChatId(usersChats.getUserId(), usersChats.getChatId());
        if (pair.isPresent()) {
            throw new InternalException(HttpStatus.BAD_REQUEST, ErrorCode.USER_ALREADY_IN_CHAT);
        }
        return usersChatsService.save(new UsersChats(UUID.randomUUID(), usersChats.getUserId(), List.of(usersChats.getChatId())));
    }
}
