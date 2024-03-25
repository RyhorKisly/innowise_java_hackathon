package innowise.com.innowise_java_hackathon.facade.impl;

import innowise.com.innowise_java_hackathon.core.dto.ChatDto;
import innowise.com.innowise_java_hackathon.core.dto.CryptocurrencyDto;
import innowise.com.innowise_java_hackathon.facade.CryptocurrencyFacade;
import innowise.com.innowise_java_hackathon.service.ChatService;
import innowise.com.innowise_java_hackathon.service.CryptocurrencyService;
import innowise.com.innowise_java_hackathon.service.MexcService;
import innowise.com.innowise_java_hackathon.telegram.InnowiseCryptocurrencyBot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@DependsOn("cryptocurrencyListener")
@RequiredArgsConstructor
public class CryptocurrencyFacadeImpl implements CryptocurrencyFacade {
    private final CryptocurrencyService cryptocurrencyService;
    private final MexcService mexcService;
    private final ChatService chatService;
    private final InnowiseCryptocurrencyBot innowiseCryptocurrencyBot;

    @Override
    @Scheduled(initialDelay = 20000, fixedDelay = 30000)
    public void updateAndNotifyWithCondition() throws TelegramApiException {
        List<CryptocurrencyDto> cryptocurrencyDtos = mexcService.getAll();
        List<CryptocurrencyDto> updatedCryptocurrencyDtos = new ArrayList<>();
        List<ChatDto> chatDtos = chatService.getAll();

        for (ChatDto chatDto : chatDtos) {
            if(chatDto.thresholdPercentage() == null) {
                continue;
            }
            for (CryptocurrencyDto cryptocurrencyDtoForUpdate : Objects.requireNonNull(cryptocurrencyDtos)) {
                CryptocurrencyDto dbCryptocurrencyDto = cryptocurrencyService.getBySymbol(cryptocurrencyDtoForUpdate);
                if(!dbCryptocurrencyDto.price().equals(cryptocurrencyDtoForUpdate.price())) {
                    CryptocurrencyDto updatedDto = cryptocurrencyService.updatePrice(cryptocurrencyDtoForUpdate);
                    if ((dbCryptocurrencyDto.price()
                            .subtract(cryptocurrencyDtoForUpdate.price())
                            .multiply(BigDecimal.valueOf(100))
                            .divide(dbCryptocurrencyDto.price(), 2, RoundingMode.HALF_UP).abs())
                            .compareTo(BigDecimal.valueOf(chatDto.thresholdPercentage())) > 0) {
                        updatedCryptocurrencyDtos.add(updatedDto);
                    }
                }
            }
            innowiseCryptocurrencyBot.cryptoMessage(updatedCryptocurrencyDtos, chatDto);
            updatedCryptocurrencyDtos.clear();
        }

    }
}
