package com.howwow.schedulebot.telegram.exception.handlers;

import com.howwow.schedulebot.config.MessageTemplates;
import com.howwow.schedulebot.exception.AlreadyExistsException;
import com.howwow.schedulebot.exception.chat.ChatNotFoundException;
import com.howwow.schedulebot.exception.chat.GroupNotFoundException;
import com.howwow.schedulebot.exception.schedule.ScheduleParseException;
import com.howwow.schedulebot.telegram.utils.TelegramMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.format.DateTimeParseException;


@Slf4j
public class ChatCommandExceptionHandler {

    public static void handleException(AbsSender absSender, Chat chat, User user, Integer messageThreadId, Exception e, String commandName) {
        switch (e) {
            case AlreadyExistsException ignored -> {
                log.warn("[{}] Бот уже активирован в чате {}", commandName, chat.getId());
                TelegramMessageSender.sendMessage(absSender, chat.getId(), messageThreadId, MessageTemplates.BOT_ALREADY_ACTIVE);
            }
            case ChatNotFoundException ignored -> {
                log.warn("[{}] Чат {} не найден", commandName, chat.getId());
                TelegramMessageSender.sendMessage(absSender, chat.getId(), messageThreadId, MessageTemplates.CHAT_NOT_FOUND_ERROR);
            }
            case DateTimeParseException ignored -> {
                log.warn("[{}] Неверный формат времени", commandName);
                TelegramMessageSender.sendMessage(absSender, chat.getId(), messageThreadId, MessageTemplates.TIME_FORMAT_ERROR);
            }
            case GroupNotFoundException ignored -> {
                log.warn("[{}] Группа не найдена", commandName);
                TelegramMessageSender.sendMessage(absSender, chat.getId(), messageThreadId, MessageTemplates.GROUP_NOT_FOUND);
            }
            case ScheduleParseException ignored -> {
                log.warn("[{}] Ошибка при парсинге расписания", commandName);
                TelegramMessageSender.sendMessage(absSender, chat.getId(), messageThreadId, MessageTemplates.SCHEDULE_PARSE_ERROR);
            }
            case null, default ->
                    GlobalCommandExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, commandName);
        }
    }
}
