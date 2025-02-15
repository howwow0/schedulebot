package com.howwow.schedulebot.config;

import com.howwow.schedulebot.telegram.utils.TelegramMessageSender;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Slf4j
public final class TelegramBot extends TelegramLongPollingCommandBot {

    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig, TelegramBotsApi telegramBotApi) throws TelegramApiException {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        telegramBotApi.registerBot(this);
    }

    @Override
    public boolean filter(Message message) {
        if (isPrivateChat(message)) {
            notifyUser(message);
            return true;
        }
        return false;
    }

    private boolean isPrivateChat(Message message) {
        Chat chat = message.getChat();
        return !(chat.isGroupChat() || chat.isSuperGroupChat());
    }

    private void notifyUser(Message message) {
        TelegramMessageSender.sendMessage(this, message.getChatId(),
                message.getMessageThreadId(),"⚠️ Бот работает только в групповых чатах\\!" );
        log.info("Отправлено предупреждение пользователю с ID {}", message.getChatId());
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        log.debug("Получено некомандное обновление: {}", update);
    }
}
