package CampusSkillSwap.repository;

import CampusSkillSwap.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

@Query("""
    SELECT message
    FROM ChatMessage message
    WHERE
        (message.senderId = :user1 AND message.receiverId = :user2)
        OR
        (message.senderId = :user2 AND message.receiverId = :user1)
    ORDER BY message.timestamp ASC, message.id ASC
    """)
List<ChatMessage> findConversation(
        @Param("user1") Long user1,
        @Param("user2") Long user2
);
}