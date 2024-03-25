package innowise.com.innowise_java_hackathon.telegram;

import innowise.com.innowise_java_hackathon.config.properties.MexcProperties;
import innowise.com.innowise_java_hackathon.core.dto.ChatDto;
import innowise.com.innowise_java_hackathon.core.enums.UserStates;
import innowise.com.innowise_java_hackathon.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import static innowise.com.innowise_java_hackathon.core.enums.UserStates.ACTIVE;
import static innowise.com.innowise_java_hackathon.core.enums.UserStates.AWAITING_NAME;
import static innowise.com.innowise_java_hackathon.core.enums.UserStates.THRESHOLD_SELECTION_STAGE;
import static innowise.com.innowise_java_hackathon.util.Constants.GREETING_MESSAGE;
import static innowise.com.innowise_java_hackathon.util.Constants.START_TEXT;

@RequiredArgsConstructor
public class ResponseHandler {
    private final SilentSender sender;
    private final ChatService chatService;
    private final KeyboardFactory keyboardFactory;
    private final MexcProperties mexcProperties;

    public void replyToStart(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(START_TEXT);
        sender.execute(message);

        chatService.save(chatId, AWAITING_NAME);
    }

    public void replyToButtons(long chatId, Message message) {
        ChatDto chatDto = chatService.getByChatId(chatId);

        if (message.getText().equalsIgnoreCase("/stop")) {
            stopChat(chatId);
            return;
        } else if (message.getText().equalsIgnoreCase("/updatethreshold")) {
            chooseThresholdMessage(chatDto);
            return;
        } else if (message.getText().equalsIgnoreCase("/currentRate")) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatDto.chatId());
            String text = "С актуальным списком валют можно ознакомиться по ссылке:\n " + mexcProperties.getApi();
            sendMessage.setText(text);
            sender.execute(sendMessage);
            return;
        }

        switch (chatDto.status()) {
            case AWAITING_NAME -> replyToName(chatDto, message);
            case THRESHOLD_SELECTION_STAGE -> replyToThresholdSelections(chatDto, message.getText());
            case ACTIVE -> activeChatMessage(chatDto);
            default -> unexpectedMessage(chatDto);
        }
    }

    public void replyToButtonsIfCallbackQuery(long chatId, String callbackQuery) {
        ChatDto chatDto = chatService.getByChatId(chatId);

        switch (chatDto.status()) {
            case THRESHOLD_SELECTION_STAGE -> replyToThresholdSelections(chatDto, callbackQuery);
            default -> unexpectedMessage(chatDto);
        }
    }

    public boolean chatIsPresent(Long chatId) {
        return chatService.isPresent(chatId);
    }

    private void unexpectedMessage(ChatDto chatDto) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatDto.chatId());
        sendMessage.setText("I did not expect that.");
        sender.execute(sendMessage);
    }

    private void stopChat(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Thank you for your time with us. See you soon!\nPress /start to begin again");
        chatService.delete(chatId);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        sender.execute(sendMessage);
    }

    private void promptWithKeyboardForState(long chatId, String text, InlineKeyboardMarkup keyboard, UserStates status) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboard);
        sender.execute(sendMessage);
        chatService.updateStatus(chatId, status);
    }

    private void replyToThresholdSelections(ChatDto chatDto, String thresholdQuery) {
        Long threshold = getThresholdFromMessage(chatDto.chatId(), thresholdQuery);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatDto.chatId());

        if (!keyboardFactory.getThresholdList().contains(threshold)) {
            sendMessage.setText("Wrong threshold! Use menu for setting threshold");
            sendMessage.setReplyMarkup(keyboardFactory.getThresholds());
            sender.execute(sendMessage);
            return;
        }
        chatService.updateThreshold(chatDto.chatId(), threshold);
        chatService.updateStatus(chatDto.chatId(), ACTIVE);

        sendMessage.setText("The " + thresholdQuery +
                " cryptocurrency threshold was successfully established.\n" +
                "If you want update threshold, use command: /updatethreshold");
        sender.execute(sendMessage);
    }

    private void activeChatMessage(ChatDto chatDto) {
        String message = "Select a command from the menu or just enjoy the functionality of the application!\n " +
                "It works especially for you!";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatDto.chatId());
        sendMessage.setText(message);
        sender.execute(sendMessage);
    }

    private Long getThresholdFromMessage(Long chatId, String callbackQuery) {
        try {
            return Long.parseLong(callbackQuery);
        } catch (NumberFormatException ex) {
            sendMessageIfThresholdDataWrong(chatId);
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    private void replyToName(ChatDto chatDto, Message message) {
        promptWithKeyboardForState(chatDto.chatId(), "Hello " + message.getText() + ".\n" + GREETING_MESSAGE,
                keyboardFactory.getThresholds(),
                THRESHOLD_SELECTION_STAGE);
    }

    private void sendMessageIfThresholdDataWrong (Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Something went wrong! " +
                "Try again specifying the cryptocurrency rate change threshold for notification");
        sendMessage.setReplyMarkup(keyboardFactory.getThresholds());
        sender.execute(sendMessage);
    };

    private void chooseThresholdMessage(ChatDto chatDto) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatDto.chatId());
        sendMessage.setText("Choose threshold!");
        sendMessage.setReplyMarkup(keyboardFactory.getThresholds());
        sender.execute(sendMessage);
        chatService.updateStatus(chatDto.chatId(), THRESHOLD_SELECTION_STAGE);
    }
}
