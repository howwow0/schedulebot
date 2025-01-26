package com.howwow.schedulebot.commands.service;

import com.howwow.schedulebot.commands.BotCommands;
import com.howwow.schedulebot.chat.dto.request.UpdateGroupNameChatSettingsRequest;
import com.howwow.schedulebot.chat.dto.response.UpdatedGroupNameChatResponse;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class UpdateGroupNameCommand extends ServiceCommand{

    private final ChatSettingsService chatSettingsService;

    public UpdateGroupNameCommand(ChatSettingsService chatSettingsService, TelegramLongPollingCommandBot telegramBot) {
        super(BotCommands.UP_GROUP_NAME.toString(), "Обновить группу 💬");
        this.chatSettingsService = chatSettingsService;
        telegramBot.register(this);
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        if(strings.length<1){
            String errorText =
                    """
                    ❌ *Ошибка: Не указано название группы*
                    
                    Пожалуйста, введите название группы после команды.
                    Пример использования:
                    */%s [Название_группы]*
                    """.formatted(BotCommands.UP_GROUP_NAME);
            sendAnswer(absSender, chat.getId(), messageThreadId,
                    this.getCommandIdentifier(), errorText);
            return;
        }

        try {
           UpdatedGroupNameChatResponse updatedGroupNameChatResponse = chatSettingsService.
                   updateGroupName(new UpdateGroupNameChatSettingsRequest(chat.getId(), strings[0]));

            String successText =
                    """
                    🔄 *Название группы успешно обновлено!*
                    
                    Все текущие уведомления и новые события теперь будут привязаны к группе *%s*.
                    Для изменения названия повторно используйте команду в нужном чате.
                    
                    """.formatted(updatedGroupNameChatResponse.groupName());

            sendAnswer(absSender, chat.getId(), messageThreadId,
                    this.getCommandIdentifier(), successText);
        } catch (NotFoundException e) {
            String errorText =
                    """
                    ⚠️ *Ошибка обновления группы!*
                    
                    Чат не найден в системе. Пожалуйста, выполните повторную настройку:
                    1. Введите команду */%s*
                    2. Перейдите в чат → */%s*
                    """.formatted(BotCommands.START, BotCommands.UP_GROUP_NAME);

            sendAnswer(absSender, chat.getId(), messageThreadId,
                    this.getCommandIdentifier(), errorText);
        }

    }
}
