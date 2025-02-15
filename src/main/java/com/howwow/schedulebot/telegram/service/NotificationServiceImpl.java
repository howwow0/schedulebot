package com.howwow.schedulebot.telegram.service;

import com.howwow.schedulebot.telegram.dto.request.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final AbsSender absSender;

    public NotificationServiceImpl(AbsSender absSender) {
        this.absSender = absSender;
    }

    @Override
    public void notify(NotificationRequest notificationRequest) {
        SendMessage message = new SendMessage();
        message.setChatId(notificationRequest.chatId());
        message.setMessageThreadId(notificationRequest.messageThreadId());
        message.setParseMode("MarkdownV2");
        message.setText(notificationRequest.text());

        try {
            absSender.execute(message);
            log.info("Уведомление успешно отправлено в чат {} (тема: {}): {}",
                    notificationRequest.chatId(),
                    notificationRequest.messageThreadId(),
                    notificationRequest.text());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке уведомления в чат {} (тема: {}): {}",
                    notificationRequest.chatId(),
                    notificationRequest.messageThreadId(),
                    e.getMessage(), e);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка при попытке отправить уведомление: {}", e.getMessage(), e);
        }
    }
}
