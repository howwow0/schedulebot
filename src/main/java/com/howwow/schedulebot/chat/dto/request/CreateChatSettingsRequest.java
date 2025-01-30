package com.howwow.schedulebot.chat.dto.request;

import lombok.Builder;

@Builder
public record CreateChatSettingsRequest(Long chatId) {
}
