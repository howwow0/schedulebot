package com.howwow.schedulebot.schedule.service.group;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GroupService {
    boolean isGroupExist(@NotNull String groupName);
}
