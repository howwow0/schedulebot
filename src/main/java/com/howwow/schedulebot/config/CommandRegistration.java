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
    private final DisableDeliveryCommand disableDeliveryCommand;
    private final UpdateDeliveryTimeCommand updateDeliveryTimeCommand;
    private final UpdateGroupNameCommand updateGroupNameCommand;
    private final UpdateMessageThreadIdCommand updateMessageThreadIdCommand;
private final TestCommand testCommand;

    @Override
    public void afterPropertiesSet() {
        telegramLongPollingCommandBot.register(startCommand);
        telegramLongPollingCommandBot.register(helpCommand);
        telegramLongPollingCommandBot.register(settingsCommand);
        telegramLongPollingCommandBot.register(disableDeliveryCommand);
        telegramLongPollingCommandBot.register(updateDeliveryTimeCommand);
        telegramLongPollingCommandBot.register(updateGroupNameCommand);
        telegramLongPollingCommandBot.register(updateMessageThreadIdCommand);
        telegramLongPollingCommandBot.register(testCommand);
    }
}
