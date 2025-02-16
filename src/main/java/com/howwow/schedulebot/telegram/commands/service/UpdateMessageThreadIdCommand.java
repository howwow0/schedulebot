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
public final class UpdateMessageThreadIdCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;

    public UpdateMessageThreadIdCommand(ChatSettingsService chatSettingsService) {
        super(BotCommands.LINK_TOPIC.toString(), "Привязать уведомления к теме чата 📌");
        this.chatSettingsService = chatSettingsService;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' пытается привязать тему уведомлений в чате {} к потоку {}",
                user.getUserName(), chat.getId(), messageThreadId);

        try {
            chatSettingsService.updateMessageThreadId(chat.getId(), messageThreadId);

            String successText = MessageTemplates.THREAD_LINKED_SUCCESS;
            sendAnswer(absSender, chat.getId(), messageThreadId, successText);
            log.info("Тема уведомлений успешно привязана в чате {}", chat.getId());

        } catch (NotFoundException e) {
            String errorText = MessageTemplates.THREAD_LINK_ERROR.formatted(BotCommands.START, BotCommands.LINK_TOPIC);
            sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
            log.warn("Чат {} не найден при попытке привязки темы", chat.getId());

        } catch (Exception e) {
            sendAnswer(absSender, chat.getId(), messageThreadId, MessageTemplates.INTERNAL_ERROR);
            log.error("Ошибка при привязке темы уведомлений в чате {}: {}", chat.getId(), e.getMessage(), e);
        }
    }
}
