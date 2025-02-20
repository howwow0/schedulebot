package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.chat.dto.request.UpdateMessageThreadIdRequest;
import com.howwow.schedulebot.chat.dto.response.UpdatedMessageThreadIdResponse;
import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.exception.NotFoundException;
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
public final class UpdateMessageThreadIdCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;

    public UpdateMessageThreadIdCommand(ChatSettingsService chatSettingsService) {
        super(BotCommands.LINK_TOPIC.toString(), "Привязать отправку расписания к теме чата 📌");
        this.chatSettingsService = chatSettingsService;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' пытается привязать тему уведомлений в чате {} к потоку {}",
                user.getUserName(), chat.getId(), messageThreadId);

        try {
           UpdatedMessageThreadIdResponse updatedMessageThreadIdResponse = chatSettingsService.updateMessageThreadId(UpdateMessageThreadIdRequest.builder()
                    .chatId(chat.getId())
                    .messageThreadId(messageThreadId)
                    .build());

            String successText = MessageTemplates.THREAD_LINKED_SUCCESS;
            sendAnswer(absSender, chat.getId(), messageThreadId, successText);
            log.info("Тема уведомлений успешно привязана в чате {}", updatedMessageThreadIdResponse.chatId());

        } catch (ConstraintViolationException e) {
            ValidationExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        } catch (Exception e) {
            ChatCommandExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        }
    }
}
