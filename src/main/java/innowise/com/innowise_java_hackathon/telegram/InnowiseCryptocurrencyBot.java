package innowise.com.innowise_java_hackathon.telegram;

import innowise.com.innowise_java_hackathon.config.properties.BotProperties;
import innowise.com.innowise_java_hackathon.config.properties.MexcProperties;
import innowise.com.innowise_java_hackathon.core.dto.ChatDto;
import innowise.com.innowise_java_hackathon.core.dto.CryptocurrencyDto;
import innowise.com.innowise_java_hackathon.service.ChatService;
import innowise.com.innowise_java_hackathon.util.Constants;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.function.BiConsumer;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

/**
 * Telegram bot class for handling InnowiseCryptocurrencyBot queries.
 */
@Component
public class InnowiseCryptocurrencyBot extends AbilityBot {

    private final ResponseHandler responseHandler;

    /**
     * Constructor for initializing the bot with necessary dependencies.
     *
     * @param botProperties The properties of the bot.
     * @param chatService The service for handling {@link org.telegram.telegrambots.meta.api.objects.Chat} operations.
     */
    public InnowiseCryptocurrencyBot(
            BotProperties botProperties,
            ChatService chatService,
            KeyboardFactory keyboardFactory,
            MexcProperties mexcProperties
    ) {
        super(botProperties.getToken(), botProperties.getName());
        responseHandler = new ResponseHandler(silent, chatService, keyboardFactory, mexcProperties);
    }


    @Override
    public long creatorId() {
        return 1L;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            String message = update.getCallbackQuery().getData();
            responseHandler.replyToButtonsIfCallbackQuery(getChatId(update), message);
        } else {
            super.onUpdateReceived(update);
        }
    }

    /**
     * Starts the new chat.
     *
     * @return The ability for starting the bot.
     */
    public Ability startBot() {
        return Ability
                .builder()
                .name("start")
                .info(Constants.START_DESCRIPTION)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> responseHandler.replyToStart(ctx.chatId()))
                .build();
    }


    /**
     * Replies to button presses.
     *
     * @return The reply for handling button presses.
     */
    public Reply replyToButtons() {
        BiConsumer<BaseAbilityBot, Update> action = (abilityBot, update) ->
            responseHandler.replyToButtons(getChatId(update), update.getMessage());
        return Reply.of(action, Flag.TEXT, update -> responseHandler.chatIsPresent(getChatId(update)));
    }

    public void cryptoMessage(List<CryptocurrencyDto> cryptocurrencyDtoForUpdate, ChatDto chatDto) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatDto.chatId());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < cryptocurrencyDtoForUpdate.size(); i++) {
            String text = "Валюта: " + cryptocurrencyDtoForUpdate.get(i).symbol() +  "; Значение: " + cryptocurrencyDtoForUpdate.get(i).price() + ".\n";
            stringBuilder.append(text);
            if(i != 0 && (i % 10 == 0)) {
                sendMessage.setText(stringBuilder.toString());
                stringBuilder.setLength(0);
                sender.execute(sendMessage);
            }
        }
        sendMessage.setText(stringBuilder.toString());
        stringBuilder.setLength(0);
        sender.execute(sendMessage);
    }
}
