package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.chat.dto.request.UpdateChatSettingsRequest;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public final class UpdateMessageThreadIdCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;

    public UpdateMessageThreadIdCommand(ChatSettingsService chatSettingsService) {
        super(BotCommands.LINK_TOPIC.toString(), "Привязать уведомления к теме чата 📌");
        this.chatSettingsService = chatSettingsService;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {

        try {
            chatSettingsService.updateMessageThreadId(new UpdateChatSettingsRequest(chat.getId(), messageThreadId));

            String successText =
                    """
                    🎯 *Тема успешно привязана!*
                    
                    Все уведомления бота теперь будут приходить в эту тему.
                    Чтобы изменить тему — вызовите команду снова в нужной ветке.
                    
                    """;

            sendAnswer(absSender, chat.getId(), messageThreadId,
                    this.getCommandIdentifier(), successText);
        } catch (NotFoundException e) {
            String errorText =
                    """
                    ⚠️ *Ошибка привязки!*
                    
                    Чат не найден в системе. Пожалуйста, выполните повторную настройку:
                    1. Введите команду */%s*
                    2. Перейдите в необходимую тему → */%s*"
                    """.formatted(BotCommands.START.toString(), BotCommands.LINK_TOPIC);

            sendAnswer(absSender, chat.getId(), messageThreadId,
                    this.getCommandIdentifier(), errorText);
        }

    }
}
