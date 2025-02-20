package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.telegram.exception.handlers.ChatCommandExceptionHandler;
import com.howwow.schedulebot.telegram.exception.handlers.ValidationExceptionHandler;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Slf4j
public class SettingsCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;

    public SettingsCommand(ChatSettingsService chatSettingsService) {
        super(BotCommands.SETTINGS.toString(), "Текущие настройки ⚙️");
        this.chatSettingsService = chatSettingsService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' запросил текущие настройки для чата {}", user.getUserName(), chat.getId());
        try {
            String formattedSettings = chatSettingsService.getFormattedChatSettings(chat.getId());
            sendAnswer(absSender, chat.getId(), messageThreadId, formattedSettings);

            log.info("Настройки чата успешно отправлены для чата {}", chat.getId());
        } catch (ConstraintViolationException e) {
            ValidationExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        } catch (Exception e) {
            ChatCommandExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        }
    }
}
