package innowise.com.innowise_java_hackathon.dao;

import innowise.com.innowise_java_hackathon.core.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    Optional<Chat> findByChatId(Long chatId);
    void deleteByChatId(Long chatId);
}
