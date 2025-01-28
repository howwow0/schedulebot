package com.howwow.schedulebot.commands.service;

import com.howwow.schedulebot.commands.BotCommands;
import com.howwow.schedulebot.service.HelpCommandService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public final class HelpCommand extends ServiceCommand {

    private final HelpCommandService helpCommandService;

    public HelpCommand(HelpCommandService helpCommandService) {
        super(BotCommands.HELP.toString(), "Справка по командам 📋");
        this.helpCommandService = helpCommandService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {

        sendAnswer(absSender, chat.getId(), messageThreadId,
                getCommandIdentifier(), helpCommandService.getFormattedHelpCommands());
    }
}