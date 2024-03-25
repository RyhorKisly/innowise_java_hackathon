package innowise.com.innowise_java_hackathon.facade;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface CryptocurrencyFacade {
    void updateAndNotifyWithCondition() throws TelegramApiException;
}
