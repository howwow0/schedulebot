package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.telegram.service.HelpCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@Slf4j
public final class HelpCommand extends ServiceCommand {

    private final HelpCommandService helpCommandService;

    public HelpCommand(HelpCommandService helpCommandService) {
        super(BotCommands.HELP.toString(), "Справка по командам 📋");
        this.helpCommandService = helpCommandService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' запросил справку по командам", user.getUserName());
        String helpText = helpCommandService.getFormattedHelpCommands();
        sendAnswer(absSender, chat.getId(), messageThreadId, helpText);
    }
}
