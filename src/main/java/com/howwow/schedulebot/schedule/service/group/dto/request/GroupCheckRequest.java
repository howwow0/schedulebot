package com.howwow.schedulebot.schedule.service.group.dto.request;

import lombok.Builder;

@Builder
public record GroupCheckRequest(String groupName) {
}
