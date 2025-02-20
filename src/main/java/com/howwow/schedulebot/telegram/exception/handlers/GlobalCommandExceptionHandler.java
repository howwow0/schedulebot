package com.howwow.schedulebot.telegram.exception.handlers;

import com.howwow.schedulebot.config.MessageTemplates;
import com.howwow.schedulebot.telegram.utils.TelegramMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public class GlobalCommandExceptionHandler {

    public static void handleException(AbsSender absSender, Chat chat, User user, Integer messageThreadId, Exception e, String commandName) {
        log.error("[{}] Критическая ошибка у пользователя {} в чате {}: {}", commandName, user.getUserName(), chat.getId(), e.getMessage(), e);
        TelegramMessageSender.sendMessage(absSender, chat.getId(), messageThreadId, MessageTemplates.INTERNAL_ERROR);
    }

}
