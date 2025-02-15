package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.telegram.utils.TelegramMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public abstract class ServiceCommand extends BotCommand {

    public ServiceCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    protected abstract void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings);

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        log.info("Обработка команды '{}', пользователь: {}, чат: {}, тема чата: {}",
                getCommandIdentifier(), message.getFrom().getUserName(), message.getChatId(), message.getMessageThreadId());
        this.execute(absSender, message.getFrom(), message.getMessageThreadId(), message.getChat(), arguments);
    }

    // Перегруженный метод без threadId (Telegram API стандарт)
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        log.warn("Вызван метод execute без messageThreadId для команды '{}'. Используем значение null.", getCommandIdentifier());
        this.execute(absSender, user, null, chat, strings);
    }

    protected void sendAnswer(AbsSender absSender, Long chatId, Integer messageThreadId, String text) {
        TelegramMessageSender.sendMessage(absSender, chatId, messageThreadId, text);
    }

}
