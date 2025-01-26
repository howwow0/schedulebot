package com.howwow.schedulebot.chat.dto.request;

import java.time.LocalTime;

public record UpdateDeliveryTimeChatSettingsRequest(Long chatId, LocalTime deliveryTime) {
}

