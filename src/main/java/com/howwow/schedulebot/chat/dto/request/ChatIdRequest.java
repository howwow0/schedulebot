package com.howwow.schedulebot.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChatIdRequest(
        @NotNull(message = "ID чата не может быть пустым")
        Long chatId
) {
}
