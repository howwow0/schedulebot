package com.howwow.schedulebot.chat.dto.response;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record UpdatedDeliveryTimeResponse(Long chatId, LocalTime deliveryTime) {
}
