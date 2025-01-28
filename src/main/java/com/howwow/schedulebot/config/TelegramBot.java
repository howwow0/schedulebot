package com.howwow.schedulebot.config;

import lombok.Getter;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
public final class TelegramBot extends TelegramLongPollingCommandBot {

    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig, TelegramBotsApi telegramBotApi) throws TelegramApiException {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        telegramBotApi.registerBot(this);
    }

    @Override
    public boolean filter(Message message) {
        Chat chat = message.getChat();
        boolean isGroup = chat.isGroupChat() || chat.isSuperGroupChat();

        if (!isGroup) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("⚠️ Бот работает только в групповых чатах!");
            sendMessage.setMessageThreadId(message.getMessageThreadId());
            sendMessage.enableMarkdown(true);
            try {
                this.execute(sendMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return !isGroup;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public void processNonCommandUpdate(Update update) {

    }
}
