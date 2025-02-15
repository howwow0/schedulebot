package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.schedule.service.group.dto.request.GroupCheckRequest;
import com.howwow.schedulebot.schedule.service.group.GroupService;
import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.chat.dto.request.UpdateGroupNameChatSettingsRequest;
import com.howwow.schedulebot.chat.dto.response.UpdatedGroupNameChatResponse;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.telegram.utils.CommandArgumentsValidator;
import com.howwow.schedulebot.telegram.utils.MarkdownV2Escaper;
import com.howwow.schedulebot.config.MessageTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Slf4j
public class UpdateGroupNameCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;
    private final GroupService groupService;

    public UpdateGroupNameCommand(ChatSettingsService chatSettingsService, GroupService groupService) {
        super(BotCommands.UP_GROUP_NAME.toString(), "Обновить группу 💬");
        this.chatSettingsService = chatSettingsService;
        this.groupService = groupService;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' пытается обновить название группы в чате {}", user.getUserName(), chat.getId());

        try {
            CommandArgumentsValidator.validateArgs(strings, 1, BotCommands.UP_GROUP_NAME.toString());
        } catch (IllegalArgumentException e) {
            String errorText = MessageTemplates.GROUP_INVALID_ARGUMENT.formatted(e.getMessage(), BotCommands.UP_GROUP_NAME);
            sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
            log.warn("Ошибка аргументов при обновлении группы: {}", e.getMessage());
            return;
        }

        String groupName = strings[0];
        log.debug("Введенное название группы: {}", groupName);

        GroupCheckRequest request = GroupCheckRequest.builder()
                .groupName(groupName)
                .build();

        if (!groupService.isGroupExist(request)) {
            sendAnswer(absSender, chat.getId(), messageThreadId, MessageTemplates.GROUP_NOT_FOUND);
            log.warn("Группа '{}' не найдена на сайте", groupName);
            return;
        }

        try {
            UpdatedGroupNameChatResponse response = chatSettingsService.updateGroupName(
                    UpdateGroupNameChatSettingsRequest.builder()
                            .chatId(chat.getId())
                            .groupName(groupName)
                            .build());

            String successText = MessageTemplates.GROUP_UPDATED_SUCCESS.formatted(MarkdownV2Escaper.escape(response.groupName()));
            sendAnswer(absSender, chat.getId(), messageThreadId, successText);
            log.info("Название группы успешно обновлено на '{}'", response.groupName());

        } catch (NotFoundException e) {
            String errorText = MessageTemplates.CHAT_NOT_FOUND_ON_UPDATE_GROUP.formatted(BotCommands.START, BotCommands.UP_GROUP_NAME);
            sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
            log.warn("Чат {} не найден при попытке обновить группу", chat.getId());

        } catch (Exception e) {
            sendAnswer(absSender, chat.getId(), messageThreadId, MessageTemplates.INTERNAL_ERROR);
            log.error("Ошибка при обновлении названия группы в чате {}: {}", chat.getId(), e.getMessage(), e);
        }
    }
}
