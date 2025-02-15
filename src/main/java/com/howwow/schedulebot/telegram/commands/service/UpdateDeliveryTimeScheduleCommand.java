package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.exception.ValidationException;
import com.howwow.schedulebot.parser.Parser;
import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.chat.dto.request.UpdateDeliveryTimeChatSettingsRequest;
import com.howwow.schedulebot.chat.dto.response.UpdatedDeliveryTimeResponse;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.telegram.utils.CommandArgumentsValidator;
import com.howwow.schedulebot.config.MessageTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.time.LocalTime;

@Component
@Slf4j
public class UpdateDeliveryTimeScheduleCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;
    private final Parser<String, LocalTime> timeParser;
    public UpdateDeliveryTimeScheduleCommand(ChatSettingsService chatSettingsService, Parser<String, LocalTime> timeParser) {
        super(BotCommands.UP_DELIVERY_TIME.toString(), "Установить время отправки расписания ⏰");
        this.chatSettingsService = chatSettingsService;
        this.timeParser = timeParser;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' пытается обновить время уведомлений в чате {}", user.getUserName(), chat.getId());

        try {
            CommandArgumentsValidator.validateArgs(strings, 1, BotCommands.UP_DELIVERY_TIME.toString());
        } catch (IllegalArgumentException e) {
            String errorText = MessageTemplates.TIME_NOT_PROVIDED.formatted(BotCommands.UP_DELIVERY_TIME);
            sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
            return;
        }

        String timeInput = strings[0];
        log.debug("Введенное время: {}", timeInput);

        LocalTime deliveryTime;
        try {
            deliveryTime = timeParser.parse(timeInput);
        } catch (IllegalArgumentException e) {
            String errorText = MessageTemplates.TIME_FORMAT_ERROR.formatted(BotCommands.UP_DELIVERY_TIME);
            sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
            return;
        }

        try {
            UpdatedDeliveryTimeResponse response = chatSettingsService.updateDeliveryTime(
                    UpdateDeliveryTimeChatSettingsRequest.builder()
                            .chatId(chat.getId())
                            .deliveryTime(deliveryTime)
                            .build());

            String successText = MessageTemplates.TIME_UPDATED_SUCCESS.formatted(response.deliveryTime().toString());
            sendAnswer(absSender, chat.getId(), messageThreadId, successText);
            log.info("Время уведомлений обновлено на {}", response.deliveryTime());

        } catch (NotFoundException e) {
            String errorText = MessageTemplates.CHAT_NOT_FOUND_ERROR.formatted(BotCommands.START);
            sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
            log.warn("Чат {} не найден", chat.getId());

        } catch (ValidationException e) {
            String errorText = MessageTemplates.TIME_FORMAT_ERROR.formatted(BotCommands.UP_DELIVERY_TIME);
            sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
            log.warn("Ошибка валидации при обновлении времени для чата {}: {}", chat.getId(), e.getMessage());

        } catch (Exception e) {
            String errorText = MessageTemplates.INTERNAL_ERROR;
            sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
            log.error("Непредвиденная ошибка при обновлении времени в чате {}: {}", chat.getId(), e.getMessage(), e);
        }
    }
}