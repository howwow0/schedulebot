package com.howwow.schedulebot.exception;

public abstract class AlreadyExistsException extends AbstractException {

    public AlreadyExistsException(String message) {
        super(message);
    }
}
