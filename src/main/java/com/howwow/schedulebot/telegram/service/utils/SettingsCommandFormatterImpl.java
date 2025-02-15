package com.howwow.schedulebot.telegram.service.utils;

import com.howwow.schedulebot.chat.dto.response.FoundedChatResponse;
import com.howwow.schedulebot.config.MessageTemplates;
import com.howwow.schedulebot.telegram.utils.MarkdownV2Escaper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class SettingsCommandFormatterImpl implements SettingsCommandFormatter {

    @Override
    public String format(FoundedChatResponse foundedChatResponse) {
        log.info("Начало форматирования настроек для чата: {}", foundedChatResponse);

        try {
            String groupName = Optional.ofNullable(foundedChatResponse.groupName())
                    .filter(name -> !name.isBlank())
                    .map(MarkdownV2Escaper::escape)
                    .orElse("Пусто");

            String deliveryTime = Optional.ofNullable(foundedChatResponse.deliveryTime())
                    .map(Object::toString)
                    .map(MarkdownV2Escaper::escape)
                    .orElse("Пусто");

            String formattedMessage = MessageTemplates.SETTINGS_TEMPLATE.formatted(groupName, deliveryTime);

            log.debug("Отформатированное сообщение: {}", formattedMessage);
            log.info("Форматирование настроек завершено для чата: {}", foundedChatResponse);
            return formattedMessage;

        } catch (Exception e) {
            log.error("Ошибка при форматировании настроек для чата {}: {}",
                    foundedChatResponse, e.getMessage(), e);
            return "⚠️ *Ошибка при отображении настроек.*";
        }
    }
}
