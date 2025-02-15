package com.howwow.schedulebot.schedule.service.group;

import com.howwow.schedulebot.schedule.service.group.dto.request.GroupCheckRequest;

public interface GroupService {
    boolean isGroupExist(GroupCheckRequest groupCheckRequest);
}
