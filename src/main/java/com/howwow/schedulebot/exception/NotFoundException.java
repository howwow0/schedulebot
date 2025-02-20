package com.howwow.schedulebot.exception;


public abstract class NotFoundException extends AbstractException {
    public NotFoundException(String message) {
        super(message);
    }
}
