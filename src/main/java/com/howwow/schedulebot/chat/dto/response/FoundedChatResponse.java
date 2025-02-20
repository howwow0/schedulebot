package com.howwow.schedulebot.chat.dto.response;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record FoundedChatResponse(Long chatId, String groupName, LocalTime deliveryTime, boolean isActive) {
}
