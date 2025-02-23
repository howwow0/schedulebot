package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.chat.dto.request.UpdateGroupNameChatSettingsRequest;
import com.howwow.schedulebot.chat.dto.response.UpdatedGroupNameChatResponse;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.telegram.exception.handlers.ChatCommandExceptionHandler;
import com.howwow.schedulebot.telegram.exception.handlers.ValidationExceptionHandler;
import com.howwow.schedulebot.telegram.utils.CommandArgumentsValidator;
import com.howwow.schedulebot.telegram.utils.MarkdownV2Escaper;
import com.howwow.schedulebot.config.MessageTemplates;
import com.howwow.schedulebot.telegram.utils.TelegramMessageSender;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Slf4j
public class UpdateGroupNameCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;

    public UpdateGroupNameCommand(ChatSettingsService chatSettingsService) {
        super(BotCommands.UP_GROUP_NAME.toString(), "Обновить группу 💬");
        this.chatSettingsService = chatSettingsService;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' пытается обновить название группы в чате {}", user.getUserName(), chat.getId());
        try {
            CommandArgumentsValidator.validateArgs(strings, 1, BotCommands.UP_GROUP_NAME.toString());
            String groupName = strings[0];
            UpdatedGroupNameChatResponse response = chatSettingsService.updateGroupName(
                    UpdateGroupNameChatSettingsRequest.builder()
                            .chatId(chat.getId())
                            .groupName(groupName)
                            .build());

            String successText = MessageTemplates.GROUP_UPDATED_SUCCESS.formatted(MarkdownV2Escaper.escape(response.groupName()));
            sendAnswer(absSender, chat.getId(), messageThreadId, successText);
            log.info("Название группы успешно обновлено на '{}'", response.groupName());

        } catch (IllegalArgumentException e) {
            log.warn("Пользователь '{}' не указал аргумент при вызове команды '{}'. Чат: {}",
                    user.getUserName(), getCommandIdentifier(), chat.getId());
            TelegramMessageSender.sendMessage(absSender, chat.getId(), messageThreadId, MessageTemplates.GROUP_INVALID_ARGUMENT);
        } catch (ConstraintViolationException e) {
            ValidationExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        } catch (Exception e) {
            ChatCommandExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        }
    }
}
