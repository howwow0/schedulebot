package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.exception.ValidationException;
import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.chat.dto.request.UpdateDeliveryTimeChatSettingsRequest;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class DisableDeliveryCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;

    public DisableDeliveryCommand(ChatSettingsService chatSettingsService) {
        super(BotCommands.DISABLE_DELIVERY.toString(), "Отключить уведомления 🔕");
        this.chatSettingsService = chatSettingsService;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        try {
            chatSettingsService.updateDeliveryTime(
                    UpdateDeliveryTimeChatSettingsRequest.builder()
                    .chatId(chat.getId())
                    .deliveryTime(null)
                    .build());

            String successText =
                    """
                    🔇 *Уведомления отключены!*
                    
                    Вы больше не будете получать ежедневные напоминания.
                    Чтобы снова активировать уведомления:
                    Используйте команду */%s [ЧЧ:ММ]*
                    """.formatted(BotCommands.UP_DELIVERY_TIME);

            sendAnswer(absSender, chat.getId(), messageThreadId,
                    this.getCommandIdentifier(), successText);

        } catch (NotFoundException e) {
            String errorText =
                    """
                    ⚠️ *Ошибка отключения!*
                    
                    Чат не найден в системе. Для активации бота:
                    1. Введите команду */%s*
                    2. Выполните первоначальную настройку
                    """.formatted(BotCommands.START);

            sendAnswer(absSender, chat.getId(), messageThreadId,
                    this.getCommandIdentifier(), errorText);
        } catch (ValidationException ignored) {
        }
    }
}