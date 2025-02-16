package user.entity;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UsersChats {

    private UUID id;

    private UUID userId;

    private List<UUID> chats;
}
