package com.howwow.schedulebot.telegram.service.utils;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;

public interface BotCommandFormatter {
    String format(IBotCommand botCommand);
}
