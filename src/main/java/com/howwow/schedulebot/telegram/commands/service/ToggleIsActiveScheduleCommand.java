package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.chat.dto.response.UpdatedIsActiveStatusResponse;
import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.config.MessageTemplates;
import com.howwow.schedulebot.telegram.exception.handlers.ChatCommandExceptionHandler;
import com.howwow.schedulebot.telegram.exception.handlers.ValidationExceptionHandler;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Slf4j
public class ToggleIsActiveScheduleCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;

    public ToggleIsActiveScheduleCommand(ChatSettingsService chatSettingsService) {
        super(BotCommands.TOGGLE_SCHEDULE.toString(), "Включить/Выключить отправку расписания 🔕");
        this.chatSettingsService = chatSettingsService;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' пытается включить/выключить расписание в чате {}", user.getUserName(), chat.getId());

        try {
            UpdatedIsActiveStatusResponse updatedIsActiveStatusResponse = chatSettingsService.toggleIsActive(chat.getId());
            String successText = updatedIsActiveStatusResponse.isActive() ? MessageTemplates.SCHEDULE_ENABLED : MessageTemplates.SCHEDULE_DISABLED;
            sendAnswer(absSender, chat.getId(), messageThreadId, successText);
            log.info("Расписание успешно включено/отключено в чате {}", chat.getId());

        } catch (ConstraintViolationException e) {
            ValidationExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        } catch (Exception e) {
            ChatCommandExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        }
    }
}
