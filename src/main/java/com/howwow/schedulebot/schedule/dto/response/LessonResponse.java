package com.howwow.schedulebot.schedule.dto.response;

import com.howwow.schedulebot.model.entities.GroupType;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record LessonResponse(String groupName,
                             int numberLesson,
                             LocalTime startTime,
                             LocalTime endTime,
                             GroupType groupType,
                             String discipline,
                             String professor,
                             String room) {
}
