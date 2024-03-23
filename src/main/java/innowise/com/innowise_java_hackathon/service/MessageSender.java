package innowise.com.innowise_java_hackathon.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender {
    void SendMessage(String message);
}
