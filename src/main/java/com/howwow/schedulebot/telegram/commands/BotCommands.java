package com.howwow.schedulebot.telegram.commands;

public enum BotCommands {
    START,
    HELP,
    SETTINGS,
    LINK_TOPIC,
    UP_GROUP_NAME,
    UP_DELIVERY_TIME,
    TOGGLE_SCHEDULE,
    SCHEDULE,
    CITE;

    @Override
    public String toString(){
        return this.name().toLowerCase();
    }
}
