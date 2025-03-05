package com.howwow.schedulebot.cite.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateCiteRequest(
        @NotBlank(message = "Цитата не может быть пустой")
        String cite,
        @NotNull(message = "ID пользователя не может быть пустым")
        Integer userId,
        @NotNull(message = "ID чата не может быть пустым")
        Long chatId
) {
}
