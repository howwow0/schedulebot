package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.exception.ValidationException;
import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.chat.dto.request.UpdateDeliveryTimeChatSettingsRequest;
import com.howwow.schedulebot.chat.dto.response.UpdatedDeliveryTimeResponse;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class UpdateDeliveryTimeCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;

    public UpdateDeliveryTimeCommand(ChatSettingsService chatSettingsService) {
        super(BotCommands.UP_DELIVERY_TIME.toString(), "Установить время уведомлений ⏰");
        this.chatSettingsService = chatSettingsService;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        if(strings.length < 1) {
            String errorText =
                    """
                    ❌ *Ошибка: Не указано время*
                    
                    Пожалуйста, введите время в формате ЧЧ:ММ.
                    Пример использования:
                    */%s 09:30*
                    """.formatted(BotCommands.UP_DELIVERY_TIME);
            sendAnswer(absSender, chat.getId(), messageThreadId,
                    this.getCommandIdentifier(), errorText);
            return;
        }

        try {
            LocalTime deliveryTime = LocalTime.parse(strings[0], DateTimeFormatter.ofPattern("HH:mm"));

                UpdatedDeliveryTimeResponse updatedDeliveryTimeResponse = chatSettingsService.
                  updateDeliveryTime(new UpdateDeliveryTimeChatSettingsRequest(chat.getId(), deliveryTime));

            String successText =
                    """
                    🕒 *Время уведомлений обновлено!*
                    
                    Ежедневное расписание будут приходить в %s.
                    Для изменения времени используйте команду повторно.
                    """.formatted(updatedDeliveryTimeResponse.deliveryTime().toString());

            sendAnswer(absSender, chat.getId(), messageThreadId,
                    this.getCommandIdentifier(), successText);

        }
        catch (NotFoundException e) {
            String errorText =
                    """
                    ⚠️ *Ошибка обновления!*
                    
                    Чат не найден в системе. Активируйте бота заново:
                    1. Введите команду */%s*
                    2. Выполните первоначальную настройку
                    """.formatted(BotCommands.START);

            sendAnswer(absSender, chat.getId(), messageThreadId,
                    this.getCommandIdentifier(), errorText);
        } catch (DateTimeParseException e) {
            String errorText =
                    """
                    ⚠️ *Некорректный формат времени!*
                    
                    Используйте формат ЧЧ:ММ (24-часовой).
                    Пример: */%s 15:30*
                    """.formatted(BotCommands.UP_DELIVERY_TIME);

            sendAnswer(absSender, chat.getId(), messageThreadId,
                    this.getCommandIdentifier(), errorText);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }
}