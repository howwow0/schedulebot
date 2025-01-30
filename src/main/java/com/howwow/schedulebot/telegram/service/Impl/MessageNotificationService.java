package com.howwow.schedulebot.telegram.service.Impl;

import com.howwow.schedulebot.telegram.dto.request.NotificationRequest;
import com.howwow.schedulebot.telegram.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class MessageNotificationService implements NotificationService {

    private final AbsSender absSender;

    @Override
    public void notify(NotificationRequest notificationRequest) {
        if (!notificationRequest.text().isEmpty()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(notificationRequest.chatId());
            sendMessage.setMessageThreadId(notificationRequest.messageThreadId());
            sendMessage.setText(notificationRequest.text());
            sendMessage.enableMarkdown(true);
            try {
                absSender.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
