package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.chat.dto.response.UpdatedIsActiveStatusResponse;
import com.howwow.schedulebot.exception.chat.ChatNotFoundException;
import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.config.MessageTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Slf4j
public class DisableDeliveryTimeScheduleCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;

    public DisableDeliveryTimeScheduleCommand(ChatSettingsService chatSettingsService) {
        super(BotCommands.DISABLE_DELIVERY.toString(), "Отключить отправку расписания 🔕");
        this.chatSettingsService = chatSettingsService;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' пытается отключить расписание в чате {}", user.getUserName(), chat.getId());

        try {
           UpdatedIsActiveStatusResponse updatedIsActiveStatusResponse = chatSettingsService.toggleIsActive(chat.getId());
        } catch (ChatNotFoundException e) {
            throw new RuntimeException(e);
        }

        String successText = MessageTemplates.SCHEDULE_DISABLED.formatted(BotCommands.UP_DELIVERY_TIME);
            sendAnswer(absSender, chat.getId(), messageThreadId, successText);
            log.info("Расписание успешно отключено в чате {}", chat.getId());

    }
}
