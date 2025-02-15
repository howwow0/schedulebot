package com.howwow.schedulebot.telegram.service;

import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.telegram.service.utils.BotCommandFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class HelpCommandServiceImpl implements HelpCommandService {

    private final BotCommandFormatter botCommandFormatter;
    private final ICommandRegistry commandRegistry;

    @Override
    public String getFormattedHelpCommands() {
        log.info("Формирование списка доступных команд");

        String formattedCommands = Arrays.stream(BotCommands.values())
                .map(cmd -> commandRegistry.getRegisteredCommands().stream()
                        .filter(c -> c.getCommandIdentifier().equals(cmd.toString()))
                        .findFirst()
                        .map(botCommandFormatter::format)
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.joining("\n"));

        String result = "📋 *Доступные команды:*\n\n" + formattedCommands;

        log.info("Сформирован список команд: {}", result);
        return result;
    }
}
