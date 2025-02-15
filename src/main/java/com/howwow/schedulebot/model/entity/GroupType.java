package com.howwow.schedulebot.model.entity;


public enum GroupType {
    FIRST, SECOND, BOTH;

    public static GroupType fromString(String groupType) {
       return switch (groupType) {
           case "1 подгруппа" -> FIRST;
           case "2 подгруппа" -> SECOND;
           default -> BOTH;
       };
    }


    public static String toString(GroupType groupType) {
        return switch (groupType) {
            case FIRST -> "1 подгруппа";
            case SECOND -> "2 подгруппа";
            case BOTH -> "";
        };
    }
}
