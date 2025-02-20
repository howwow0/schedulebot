package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.chat.dto.request.ChatIdRequest;
import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.config.MessageTemplates;
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
public final class StartCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;

    public StartCommand(ChatSettingsService chatSettingsService) {
        super(BotCommands.START.toString(), "Начать работу с ботом ▶️");
        this.chatSettingsService = chatSettingsService;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' пытается активировать бота в чате {}", user.getUserName(), chat.getId());

        try {
            chatSettingsService.create(ChatIdRequest.builder()
                    .chatId(chat.getId())
                    .build());

            String responseText = MessageTemplates.BOT_ACTIVATED_MESSAGE;
            sendAnswer(absSender, chat.getId(), messageThreadId, responseText);
            sendAnswer(absSender, chat.getId(), messageThreadId, MessageTemplates.ABOUT_BOT);
            log.info("Бот успешно активирован для чата {}", chat.getId());

        } catch (ConstraintViolationException e) {
            ValidationExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        } catch (Exception e) {
            ChatCommandExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        }

    }
}
