package com.howwow.schedulebot.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record UpdateDeliveryTimeChatSettingsRequest(
        @NotNull
        Long chatId,
        @NotNull
        LocalTime deliveryTime) {
}

