package innowise.com.innowise_java_hackathon.service.impl;

import innowise.com.innowise_java_hackathon.config.properties.BotProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotProperties botProperties;

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }



    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        System.out.println(update);
        System.out.println(update.getMessage().getChatId());
        System.out.println(user.getFirstName() + " wrote " + msg.getText());
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }
}
