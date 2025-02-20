package com.howwow.schedulebot.exception.chat;

import com.howwow.schedulebot.exception.AlreadyExistsException;

public class ChatAlreadyExistsException extends AlreadyExistsException {
    public ChatAlreadyExistsException(Long chatId) {
        super("Chat with id " + chatId + " already exists");
    }
}
