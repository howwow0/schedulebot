package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.exception.AlreadyExistsException;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.config.MessageTemplates;
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
            chatSettingsService.create(chat.getId());

            String responseText = MessageTemplates.BOT_ACTIVATED_MESSAGE.formatted(BotCommands.HELP, BotCommands.SETTINGS);
            sendAnswer(absSender, chat.getId(), messageThreadId, responseText);
            sendAnswer(absSender, chat.getId(), messageThreadId, MessageTemplates.ABOUT_BOT);
            log.info("Бот успешно активирован для чата {}", chat.getId());

        } catch (AlreadyExistsException e) {
            log.warn("Попытка повторной активации бота в чате {}", chat.getId());
            String errorText = MessageTemplates.BOT_ALREADY_ACTIVE.formatted(BotCommands.SETTINGS);
            sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
        } catch (Exception e) {
            log.error("Ошибка при активации бота в чате {}: {}", chat.getId(), e.getMessage(), e);
            sendAnswer(absSender, chat.getId(), messageThreadId, MessageTemplates.INTERNAL_ERROR);
        }
    }
}
