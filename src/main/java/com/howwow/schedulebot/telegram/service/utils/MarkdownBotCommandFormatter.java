package com.howwow.schedulebot.telegram.service.utils;

import com.howwow.schedulebot.telegram.utils.MarkdownV2Escaper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;

@Component
@Slf4j
public class MarkdownBotCommandFormatter implements BotCommandFormatter {

    @Override
    public String format(IBotCommand botCommand) {
        log.info("Начинаем форматирование команды: '{}', описание: '{}'",
                botCommand.getCommandIdentifier(), botCommand.getDescription());

        try {
            String command = MarkdownV2Escaper.escape(botCommand.getCommandIdentifier());
            String description = MarkdownV2Escaper.escape(botCommand.getDescription());

            String formattedMessage = "`/%s` — *%s*".formatted(command, description);

            log.debug("Отформатированная команда: {}", formattedMessage);
            return formattedMessage;

        } catch (Exception e) {
            log.error("Ошибка при форматировании команды '{}' с описанием '{}': {}",
                    botCommand.getCommandIdentifier(),
                    botCommand.getDescription(),
                    e.getMessage(), e);
            return "⚠️ Ошибка форматирования команды.";
        }
    }
}
