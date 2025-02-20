package com.howwow.schedulebot.chat.utils;

import com.howwow.schedulebot.config.MessageTemplates;
import com.howwow.schedulebot.model.entity.ChatSettings;
import com.howwow.schedulebot.telegram.utils.MarkdownV2Escaper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class SettingsFormatterImpl implements SettingsFormatter {

    @Override
    public String format(ChatSettings chatSettings) {
        log.info("Начало форматирования настроек для чата: {}", chatSettings);

        String groupName = Optional.ofNullable(chatSettings.getGroupName())
                .filter(name -> !name.isBlank())
                .map(MarkdownV2Escaper::escape)
                .orElse("Пусто");

        String deliveryTime = Optional.ofNullable(chatSettings.getDeliveryTime())
                .map(Object::toString)
                .map(MarkdownV2Escaper::escape)
                .orElse("Пусто");

        String isActive = Optional.ofNullable(chatSettings.getIsActive())
                .map(p -> p ? "✅" : "❌")
                .map(MarkdownV2Escaper::escape)
                .orElse("Пусто");

        String formattedMessage = MessageTemplates.SETTINGS_TEMPLATE.formatted(groupName, deliveryTime, isActive);

        log.debug("Отформатированное сообщение: {}", formattedMessage);
        log.info("Форматирование настроек завершено для чата: {}", chatSettings);
        return formattedMessage;
    }
}
