package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.chat.dto.request.FindChatRequest;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.telegram.service.utils.SettingsCommandFormatter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
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

        try {
            sendAnswer(absSender, chat.getId(), messageThreadId,
                    getCommandIdentifier(), settingsCommandFormatter.format(
                            chatSettingsService.findByChatId(
                                    new FindChatRequest(chat.getId()))));
        } catch (NotFoundException e) {
            String errorText =
                    """
                    ⚠️ *Ошибка команды!*
                    
                    Чат не найден в системе. Активируйте бота заново:
                    1. Введите команду */%s*
                    2. Выполните первоначальную настройку
                    """.formatted(BotCommands.START);

            sendAnswer(absSender, chat.getId(), messageThreadId,
                    this.getCommandIdentifier(), errorText);
        }
    }
}