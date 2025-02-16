package messenger.service;

import messenger.entity.UsersChats;
import messenger.repository.chat.UsersChatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersChatsService {

    private final UsersChatsRepository usersChatsRepository;

    @Autowired
    public UsersChatsService(UsersChatsRepository usersChatsRepository) {
        this.usersChatsRepository = usersChatsRepository;
    }

    public Optional<UsersChats> findByUserId(UUID uuid) {
        return usersChatsRepository.findByUserId(uuid);
    }

    public UsersChats save(UsersChats usersChats) {
        return usersChatsRepository.save(usersChats);
    }

    public List<UUID> findIdsByChatId(UUID uuid) {
        return usersChatsRepository.findIdsByChatId(uuid);
    }

    public Optional<UsersChats> findById(UUID uuid) {
        return usersChatsRepository.findById(uuid);
    }

    public int countByChatId(UUID uuid) {
        return usersChatsRepository.countByChatId(uuid);
    }
}
