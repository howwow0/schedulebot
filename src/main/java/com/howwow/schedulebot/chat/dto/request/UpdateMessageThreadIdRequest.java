package com.howwow.schedulebot.chat.dto.request;

import com.howwow.schedulebot.config.MessageTemplates;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateMessageThreadIdRequest(
        @NotNull
        Long chatId,
        @NotNull(message = MessageTemplates.MESSAGE_THREAD_NOT_EXISTS)
        Integer messageThreadId
) {
}
