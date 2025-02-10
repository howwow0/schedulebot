package com.howwow.schedulebot.schedule.utils;

import com.howwow.schedulebot.model.entities.Lesson;

import java.util.List;

public interface ScheduleParser {
    List<Lesson> parse(String groupName);
}
