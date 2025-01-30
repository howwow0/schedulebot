package com.howwow.schedulebot.telegram.dto.request;

import lombok.Builder;

@Builder
public record NotificationRequest(Long chatId, Integer messageThreadId, String text) {
}
