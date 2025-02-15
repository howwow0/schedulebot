package com.howwow.schedulebot.telegram.commands.service;

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
            chatSettingsService.removeDeliveryTime(chat.getId());

            String successText = MessageTemplates.SCHEDULE_DISABLED.formatted(BotCommands.UP_DELIVERY_TIME);
            sendAnswer(absSender, chat.getId(), messageThreadId, successText);
            log.info("Расписание успешно отключено в чате {}", chat.getId());

        } catch (NotFoundException e) {
            String errorText = MessageTemplates.CHAT_NOT_FOUND_ERROR.formatted(BotCommands.START);
            sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
            log.warn("Чат {} не найден при попытке отключить расписание", chat.getId());

        } catch (Exception e) {
            sendAnswer(absSender, chat.getId(), messageThreadId, MessageTemplates.INTERNAL_ERROR);
            log.error("Ошибка при отключении расписания в чате {}: {}", chat.getId(), e.getMessage(), e);
        }
    }
}
