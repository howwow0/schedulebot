package com.howwow.schedulebot.chat.dto.request;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record UpdateDeliveryTimeChatSettingsRequest(Long chatId, LocalTime deliveryTime) {
}

