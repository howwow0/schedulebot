package com.howwow.schedulebot.telegram.exception.handlers;

import com.howwow.schedulebot.telegram.utils.TelegramMessageSender;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.stream.Collectors;

@Slf4j
public class ValidationExceptionHandler {

    public static void handleException(AbsSender absSender, Chat chat, User user, Integer messageThreadId, Exception e, String commandName) {
        String errorMessage;

        if (e instanceof ConstraintViolationException constraintViolationException) {
            errorMessage = constraintViolationException.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("\n"));
            log.warn("[{}] Ошибка валидации у пользователя {}: {}", commandName, user.getUserName(), errorMessage);
            TelegramMessageSender.sendMessage(absSender, chat.getId(), messageThreadId, errorMessage);
        } else {
            log.error("[{}] Неизвестная ошибка валидации у пользователя {}: {}", commandName, user.getUserName(), e.getMessage(), e);
            GlobalCommandExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, commandName);
        }
    }


}
