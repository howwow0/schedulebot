package com.howwow.schedulebot.commands.service;

import com.howwow.schedulebot.commands.BotCommands;
import com.howwow.schedulebot.chat.dto.request.CreateChatSettingsRequest;
import com.howwow.schedulebot.exception.AlreadyExistsException;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public final class StartCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;

    public StartCommand(ChatSettingsService chatSettingsService) {
        super(BotCommands.START.toString(), "Начать работу с ботом ▶️");
        this.chatSettingsService = chatSettingsService;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        try {

            chatSettingsService.create(new CreateChatSettingsRequest(chat.getId()));
            String responseText =
                    """
                            ✅ *Бот активирован!*
                            Основные команды:
                            */%s* — список команд"
                            */%s* — текущие настройки""".formatted(BotCommands.HELP, BotCommands.SETTINGS);

            sendAnswer(absSender, chat.getId(), messageThreadId, this.getCommandIdentifier(),
                    responseText);

        } catch (AlreadyExistsException e) {
            String errorText =
                    """
                            ⚠️ Бот уже активирован для этого чата." +
                            "Используйте */%s* для просмотра параметров.""".formatted(BotCommands.SETTINGS);
            sendAnswer(absSender, chat.getId(), messageThreadId, this.getCommandIdentifier(),
                    errorText);
        }

    }
}