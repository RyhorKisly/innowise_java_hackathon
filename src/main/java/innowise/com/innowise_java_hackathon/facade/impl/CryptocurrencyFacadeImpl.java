package innowise.com.innowise_java_hackathon.facade.impl;

import innowise.com.innowise_java_hackathon.core.dto.CryptocurrencyDto;
import innowise.com.innowise_java_hackathon.facade.CryptocurrencyFacade;
import innowise.com.innowise_java_hackathon.service.CryptocurrencyService;
import innowise.com.innowise_java_hackathon.service.feign.CryptocurrencyServiceClient;
import innowise.com.innowise_java_hackathon.service.impl.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CryptocurrencyFacadeImpl implements CryptocurrencyFacade {
    private final CryptocurrencyService cryptocurrencyService;
    private final CryptocurrencyServiceClient cryptocurrencyServiceClient;
    private final TelegramBot telegramBot;


    @Override
    @Scheduled(fixedDelay = 20000)
    public void checkAndNotify() {
        List<CryptocurrencyDto> cryptocurrencyDtos = cryptocurrencyServiceClient.getAll().getBody();
        List<CryptocurrencyDto> beforeUpdateDto = cryptocurrencyService.getAll();
        List<CryptocurrencyDto> updatedCryptocurrencyDtos = new ArrayList<>();

        telegramBot.sendText(Long.valueOf("412192956"), "обновление данных");

        for (CryptocurrencyDto cryptocurrencyDto : Objects.requireNonNull(cryptocurrencyDtos)) {
            CryptocurrencyDto updatedDto = cryptocurrencyService.update(cryptocurrencyDto);
            if(updatedDto.isUpdated()) {
                updatedCryptocurrencyDtos.add(updatedDto);
                String message = "Валюта: " + cryptocurrencyDto.symbol() + "была обновлена.";
                telegramBot.sendText(Long.valueOf("412192956"), message);
            }
        }
    }
}
