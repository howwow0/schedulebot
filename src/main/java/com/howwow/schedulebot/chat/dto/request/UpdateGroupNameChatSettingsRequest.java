package com.howwow.schedulebot.chat.dto.request;

import com.howwow.schedulebot.config.MessageTemplates;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateGroupNameChatSettingsRequest(
        @NotNull(message = MessageTemplates.INTERNAL_ERROR)
        Long chatId,
        @NotBlank(message = MessageTemplates.GROUP_INVALID_ARGUMENT)
        String groupName) {
}
