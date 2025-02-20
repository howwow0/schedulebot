package com.howwow.schedulebot.exception.chat;

import com.howwow.schedulebot.exception.NotFoundException;

public class ChatNotFoundException extends NotFoundException {
    public ChatNotFoundException(Long chatId) {
        super("Chat with id " + chatId + " not found");
    }
}
