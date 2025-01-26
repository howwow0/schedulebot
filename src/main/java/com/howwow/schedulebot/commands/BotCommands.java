package com.howwow.schedulebot.commands;

public enum BotCommands {
    START,
    HELP,
    SETTINGS,
    LINK_TOPIC,
    SCHEDULE,
    UP_SCHEDULE,
    UP_GROUP_NAME,
    UP_DELIVERY_TIME,
    DISABLE_DELIVERY;

    @Override
    public String toString(){
        return this.name().toLowerCase();
    }
}
