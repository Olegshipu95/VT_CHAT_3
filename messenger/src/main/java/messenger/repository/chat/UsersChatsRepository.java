package messenger.repository.chat;

import messenger.entity.UsersChats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UsersChatsRepository extends JpaRepository<UsersChats, UUID> {

    @Query(value = """
            select * from users_chats
            left join users_chats_chats on users_chats.id = users_chats_chats.users_chats_id
            where users_chats.user_id = :id and users_chats_chats.chats_ids = :chatId
            """, nativeQuery = true)
    Optional<UsersChats> findByUserIdAndChatId(UUID id, UUID chatId);

    List<UsersChats> findByUserId(UUID userId);

    @Query("SELECT uc.id FROM UsersChats uc WHERE :chatId MEMBER OF uc.chats")
    List<UUID> findIdsByChatId(@Param("chatId") UUID chatId);

    @Query(value = "SELECT COUNT(id) FROM Users_Chats as uc WHERE :chatId = ANY(users_chats_id)", nativeQuery = true)
    int countByChatId(@Param("chatId") UUID chatId);
}
