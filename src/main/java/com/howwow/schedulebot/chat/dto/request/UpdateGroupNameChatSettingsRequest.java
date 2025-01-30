package com.howwow.schedulebot.chat.dto.request;

import lombok.Builder;

@Builder
public record UpdateGroupNameChatSettingsRequest(Long chatId, String groupName) {
}
