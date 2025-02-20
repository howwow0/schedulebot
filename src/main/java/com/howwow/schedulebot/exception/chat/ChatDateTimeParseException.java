package com.howwow.schedulebot.exception.chat;

import java.time.format.DateTimeParseException;

public class ChatDateTimeParseException extends DateTimeParseException {
    public ChatDateTimeParseException(String parsedString, CharSequence text, int errorIndex) {
        super(parsedString, text, errorIndex);
    }
}
