package com.howwow.schedulebot.telegram.service.utils.Impl;

import com.howwow.schedulebot.telegram.service.utils.BotCommandFormatter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;

@Component
public class MarkdownBotCommandFormatter implements BotCommandFormatter {
    @Override
    public String format(IBotCommand botCommand) {
        return String.format(
                "*/%s* — *%s*",
                botCommand.getCommandIdentifier(),
                botCommand.getDescription()
        );
    }
}
