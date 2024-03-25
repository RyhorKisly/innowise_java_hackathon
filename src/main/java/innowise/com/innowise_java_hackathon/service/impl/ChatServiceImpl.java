package innowise.com.innowise_java_hackathon.service.impl;

import innowise.com.innowise_java_hackathon.core.dto.ChatDto;
import innowise.com.innowise_java_hackathon.core.entity.Chat;
import innowise.com.innowise_java_hackathon.core.enums.UserStates;
import innowise.com.innowise_java_hackathon.core.exceptions.FindEntityException;
import innowise.com.innowise_java_hackathon.core.mapper.ChatMapper;
import innowise.com.innowise_java_hackathon.dao.ChatRepository;
import innowise.com.innowise_java_hackathon.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    @Override
    @Transactional
    public void save(Long chatId, UserStates userStates) {
        Chat chat = new Chat();
        chat.setChatId(chatId);
        chat.setStatus(userStates);
        chatRepository.save(chat);
    }

    @Override
    @Transactional(readOnly = true)
    public ChatDto getByChatId(Long chatId) {
        Chat chat = chatRepository.findByChatId(chatId)
                .orElseThrow(() -> new FindEntityException("chat not found"));
        return chatMapper.toDto(chat);
    }

    @Override
    @Transactional
    public void delete(Long chatId) {
        chatRepository.deleteByChatId(chatId);
    }

    @Override
    @Transactional
    public boolean isPresent(Long chatId) {
        return chatRepository.findByChatId(chatId).isPresent();
    }

    @Override
    @Transactional
    public void updateStatus(Long chatId, UserStates status) {
        Chat chat = chatRepository.findByChatId(chatId).orElseThrow(() -> new FindEntityException("chat not found"));
        chat.setStatus(status);
        chatRepository.save(chat);
    }

    @Override
    @Transactional
    public void updateThreshold(Long chatId, Long threshold) {
        Chat chat = chatRepository.findByChatId(chatId).orElseThrow(() -> new FindEntityException("chat not found"));
        chat.setThresholdPercentage(threshold);
        chatRepository.save(chat);
    }

    @Override
    public List<ChatDto> getAll() {
        List<Chat> chats = chatRepository.findAll();
        return chatMapper.toDtos(chats);
    }
}
