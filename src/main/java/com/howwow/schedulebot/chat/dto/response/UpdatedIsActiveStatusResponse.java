package com.howwow.schedulebot.chat.dto.response;

import lombok.Builder;

@Builder
public record UpdatedIsActiveStatusResponse(
        Long chatId,
        Boolean isActive
) {
}
