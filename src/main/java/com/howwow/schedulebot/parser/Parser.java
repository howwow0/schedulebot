package com.howwow.schedulebot.parser;

public interface Parser<T, R> {
    R parse(T input);
}