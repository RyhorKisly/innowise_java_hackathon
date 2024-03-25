package innowise.com.innowise_java_hackathon.service;

import innowise.com.innowise_java_hackathon.core.dto.ChatDto;
import innowise.com.innowise_java_hackathon.core.entity.Chat;
import innowise.com.innowise_java_hackathon.core.enums.UserStates;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    void save(Long chatId, UserStates userStates);
    ChatDto getByChatId(Long chatId);
    void delete(Long chatId);
    boolean isPresent(Long chatId);
    void updateStatus(Long chatId, UserStates status);
    void updateThreshold(Long chatId, Long threshold);
    List<ChatDto> getAll();

}
