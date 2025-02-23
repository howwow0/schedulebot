package com.howwow.schedulebot.config;

import com.howwow.schedulebot.telegram.commands.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;

@Component
@RequiredArgsConstructor
public class CommandRegistration implements InitializingBean {

    private final TelegramLongPollingCommandBot telegramLongPollingCommandBot;
    private final StartCommand startCommand;
    private final HelpCommand helpCommand;
    private final SettingsCommand settingsCommand;
    private final ToggleIsActiveScheduleCommand toggleIsActiveScheduleCommand;
    private final UpdateDeliveryTimeScheduleCommand updateDeliveryTimeScheduleCommand;
    private final UpdateGroupNameCommand updateGroupNameCommand;
    private final UpdateMessageThreadIdCommand updateMessageThreadIdCommand;
    private final SendScheduleCommand sendScheduleCommand;
    @Override
    public void afterPropertiesSet() {
        telegramLongPollingCommandBot.register(startCommand);
        telegramLongPollingCommandBot.register(helpCommand);
        telegramLongPollingCommandBot.register(settingsCommand);
        telegramLongPollingCommandBot.register(toggleIsActiveScheduleCommand);
        telegramLongPollingCommandBot.register(updateDeliveryTimeScheduleCommand);
        telegramLongPollingCommandBot.register(updateGroupNameCommand);
        telegramLongPollingCommandBot.register(updateMessageThreadIdCommand);
        telegramLongPollingCommandBot.register(sendScheduleCommand);
    }
}
