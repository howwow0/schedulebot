package com.howwow.schedulebot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class AbstractException extends Exception {

    protected final String message;

}
