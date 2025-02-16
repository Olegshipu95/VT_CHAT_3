package messenger.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users_chats")
public class UsersChats {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "chats_ids")
    @ElementCollection
    @CollectionTable(name = "users_chats_chats", joinColumns = @JoinColumn(name = "users_chats_id"))
    private List<UUID> chats;
}
