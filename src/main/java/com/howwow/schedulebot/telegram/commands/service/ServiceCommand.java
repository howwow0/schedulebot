package com.howwow.schedulebot.telegram.commands.service;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class ServiceCommand extends BotCommand {

    public ServiceCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    protected abstract void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings);

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        this.execute(absSender, message.getFrom(), message.getMessageThreadId(), message.getChat(), arguments);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {}

    protected void sendAnswer(AbsSender absSender, Long chatId, Integer messageThreadId, String commandName, String text) {
        SendMessage message = new SendMessage();
        message.setMessageThreadId(messageThreadId);
        message.enableMarkdown(true);
        message.setChatId(chatId);
        message.setText(text);
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
