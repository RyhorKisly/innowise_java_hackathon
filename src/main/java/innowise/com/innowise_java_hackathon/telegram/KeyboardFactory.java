package innowise.com.innowise_java_hackathon.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class KeyboardFactory {

    private final List<Long> thresholdList = new ArrayList<>(List.of(3L, 5L, 10L, 50L));
    public ReplyKeyboard getPizzaToppingsKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add("Margherita");
        row.add("Pepperoni");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public ReplyKeyboard getYesOrNo() {
        KeyboardRow row = new KeyboardRow();
        row.add("Yes");
        row.add("No");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public InlineKeyboardMarkup getThresholds() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (Long threshold : thresholdList) {
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(threshold + "%");
            inlineKeyboardButton.setCallbackData(threshold.toString());
            row.add(inlineKeyboardButton);
        }
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public List<Long> getThresholdList() {
        return thresholdList;
    }
}