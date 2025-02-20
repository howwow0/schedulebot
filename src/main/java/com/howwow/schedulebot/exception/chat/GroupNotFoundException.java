package com.howwow.schedulebot.exception.chat;

import com.howwow.schedulebot.exception.NotFoundException;

public class GroupNotFoundException extends NotFoundException {
    public GroupNotFoundException(String groupName) {
        super("Group with name " + groupName + " not found");
    }
}
