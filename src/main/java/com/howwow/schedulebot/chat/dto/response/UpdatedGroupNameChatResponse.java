package com.howwow.schedulebot.chat.dto.response;

import lombok.Builder;

@Builder
public record UpdatedGroupNameChatResponse(Long chatId, String groupName) {
}
