package com.howwow.schedulebot.telegram.utils;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class TelegramMessageSender {

    public static void sendMessage(AbsSender absSender, Long chatId, Integer threadId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setMessageThreadId(threadId);
        message.setParseMode("MarkdownV2");
        message.setText(text);

        try {
            absSender.execute(message);
            log.info("Сообщение успешно отправлено в чат {}: {}", chatId, text);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения в чат {}: {}", chatId, e.getMessage(), e);
        }
    }
}
