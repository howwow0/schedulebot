package com.howwow.schedulebot.chat.dto.response;

import lombok.Builder;

@Builder
public record UpdatedMessageThreadIdResponse(
        Long chatId,
        Integer messageThreadId
) {
}
