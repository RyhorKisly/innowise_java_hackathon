package innowise.com.innowise_java_hackathon.core.dto;

import innowise.com.innowise_java_hackathon.core.enums.UserStates;


public record ChatDto(
        Long chatId,
        Long thresholdPercentage,
        UserStates status
) {
}
