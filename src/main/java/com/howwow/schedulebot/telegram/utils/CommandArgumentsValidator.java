package com.howwow.schedulebot.telegram.utils;


public class CommandArgumentsValidator {

    public static void validateArgs(String[] args, int requiredArgsCount, String commandName) {
        if (args == null || args.length < requiredArgsCount) {
            throw new IllegalArgumentException("Недостаточно аргументов для команды: " + commandName);
        }
    }
}
