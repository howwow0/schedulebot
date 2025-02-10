package com.howwow.schedulebot.schedule.dto.request;

import com.howwow.schedulebot.model.entities.GroupType;

import java.time.LocalTime;

public record LessonRequest(String groupName,
                           int numberLesson,
                           LocalTime startTime,
                           LocalTime endTime,
                           GroupType groupType,
                           String discipline,
                           String professor,
                           String room) {
}
