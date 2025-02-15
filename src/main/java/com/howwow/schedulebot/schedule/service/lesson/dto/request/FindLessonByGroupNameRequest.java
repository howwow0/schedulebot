package com.howwow.schedulebot.schedule.service.lesson.dto.request;

import lombok.Builder;

@Builder
public record FindLessonByGroupNameRequest(
        String groupName) {
}
