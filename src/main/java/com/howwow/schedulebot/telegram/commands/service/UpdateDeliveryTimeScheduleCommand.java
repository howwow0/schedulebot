package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.parser.Parser;
import com.howwow.schedulebot.parser.time.TimeParser;
import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.chat.dto.request.UpdateDeliveryTimeChatSettingsRequest;
import com.howwow.schedulebot.chat.dto.response.UpdatedDeliveryTimeResponse;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.telegram.exception.handlers.ChatCommandExceptionHandler;
import com.howwow.schedulebot.telegram.exception.handlers.ValidationExceptionHandler;
import com.howwow.schedulebot.config.MessageTemplates;
import com.howwow.schedulebot.telegram.utils.CommandArgumentsValidator;
import com.howwow.schedulebot.telegram.utils.TelegramMessageSender;
import jakarta.validation.ConstraintViolationException;
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
    private final TimeParser timeParser;

    public UpdateDeliveryTimeScheduleCommand(ChatSettingsService chatSettingsService, TimeParser timeParser) {
        super(BotCommands.UP_DELIVERY_TIME.toString(), "Установить время отправки расписания ⏰");
        this.chatSettingsService = chatSettingsService;
        this.timeParser = timeParser;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' пытается обновить время уведомлений в чате {}", user.getUserName(), chat.getId());
        try {
            CommandArgumentsValidator.validateArgs(strings, 1, getCommandIdentifier());
            LocalTime deliveryTime = timeParser.parse(strings[0]);
            UpdatedDeliveryTimeResponse response = chatSettingsService.updateDeliveryTime(
                    UpdateDeliveryTimeChatSettingsRequest.builder()
                            .chatId(chat.getId())
                            .deliveryTime(deliveryTime)
                            .build());

            String successText = MessageTemplates.TIME_UPDATED_SUCCESS.formatted(response.deliveryTime().toString());
            sendAnswer(absSender, chat.getId(), messageThreadId, successText);
            log.info("Время уведомлений обновлено на {}", response.deliveryTime());

        } catch (IllegalArgumentException e) {
            log.warn("Пользователь '{}' не указал аргумент при вызове команды '{}'. Чат: {}",
                    user.getUserName(), getCommandIdentifier(), chat.getId());
            TelegramMessageSender.sendMessage(absSender, chat.getId(), messageThreadId, MessageTemplates.TIME_NOT_PROVIDED);
        } catch (ConstraintViolationException e) {
            ValidationExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        } catch (Exception e) {
            ChatCommandExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        }
    }
}