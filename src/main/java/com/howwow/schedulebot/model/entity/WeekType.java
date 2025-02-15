package com.howwow.schedulebot.model.entity;

public enum WeekType {
    EVEN, ODD, BOTH;

    public static WeekType fromString(String weekType) {
        return switch (weekType) {
            case "Четная" -> EVEN;
            case "Нечетная" -> ODD;
            default -> BOTH;
        };
    }

    public static String toString(WeekType weekType) {
        return switch (weekType) {
            case EVEN -> "Четная";
            case ODD -> "Нечетная";
            case BOTH -> "";
        };
    }
}
