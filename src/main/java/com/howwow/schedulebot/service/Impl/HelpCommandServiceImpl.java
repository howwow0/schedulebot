package com.howwow.schedulebot.service.Impl;

import com.howwow.schedulebot.commands.BotCommands;
import com.howwow.schedulebot.service.HelpCommandService;
import com.howwow.schedulebot.service.utils.BotCommandFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.ICommandRegistry;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HelpCommandServiceImpl implements HelpCommandService {

    private final BotCommandFormatter botCommandFormatter;
    private final ICommandRegistry commandRegistry;

    @Override
    public String getFormattedHelpCommands() {
        Map<String, IBotCommand> registeredCommands = commandRegistry.getRegisteredCommands().stream()
                .collect(Collectors.toMap(
                        IBotCommand::getCommandIdentifier,
                        Function.identity()
                ));

        String formattedCommands = Arrays.stream(BotCommands.values())
                .filter(cmd -> registeredCommands.containsKey(cmd.toString()))
                .map(cmd -> {
                    IBotCommand command = registeredCommands.get(cmd.toString());
                    return botCommandFormatter.format(command);
                })
                .collect(Collectors.joining("\n"));

        return "📋 *Доступные команды:*\n\n" + formattedCommands;
    }
}
