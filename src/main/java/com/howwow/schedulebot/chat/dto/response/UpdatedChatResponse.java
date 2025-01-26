package com.howwow.schedulebot.chat.dto.response;

import lombok.Builder;

@Builder
public record UpdatedChatResponse(Long chatId, Integer messageThreadId) {
}
