package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.telegram.service.utils.SettingsCommandFormatter;
import com.howwow.schedulebot.config.MessageTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Slf4j
public class SettingsCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;
    private final SettingsCommandFormatter settingsCommandFormatter;

    public SettingsCommand(ChatSettingsService chatSettingsService, SettingsCommandFormatter settingsCommandFormatter) {
        super(BotCommands.SETTINGS.toString(), "Текущие настройки ⚙️");
        this.chatSettingsService = chatSettingsService;
        this.settingsCommandFormatter = settingsCommandFormatter;
    }

    @Override
    public void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' запросил текущие настройки для чата {}", user.getUserName(), chat.getId());

        try {
            var chatSettings = chatSettingsService.findByChatId(chat.getId());
            String formattedSettings = settingsCommandFormatter.format(chatSettings);
            sendAnswer(absSender, chat.getId(), messageThreadId, formattedSettings);

            log.info("Настройки чата успешно отправлены для чата {}", chat.getId());

        } catch (NotFoundException e) {
            log.warn("Чат с ID {} не найден", chat.getId());
            String errorText = MessageTemplates.CHAT_NOT_FOUND_ERROR.formatted(BotCommands.START);
            sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
        }
        catch (Exception e) {
            log.error("Ошибка при отправки настроек в чате {}: {}", chat.getId(), e.getMessage(), e);
            sendAnswer(absSender, chat.getId(), messageThreadId, MessageTemplates.INTERNAL_ERROR);
        }
    }
}
