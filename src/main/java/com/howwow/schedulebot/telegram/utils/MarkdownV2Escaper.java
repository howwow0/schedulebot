package com.howwow.schedulebot.telegram.utils;

public class MarkdownV2Escaper {

    /**
     * Экранирует строку для MarkdownV2.
     * @param text Исходная строка
     * @return Экранированная строка
     */
    public static String escape(String text) {
        if (text == null) return "";
        return text.replaceAll("([_\\*\\[\\]()~`>#+\\-=|{}.!])", "\\\\$1");
    }
}
