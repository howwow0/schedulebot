package com.howwow.schedulebot.schedule.utils;

import com.howwow.schedulebot.model.entities.Lesson;

import java.util.List;

public interface LessonFormatter {
    String format(List<Lesson> lessons);
}
