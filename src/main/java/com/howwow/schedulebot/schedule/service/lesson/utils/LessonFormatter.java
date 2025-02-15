package com.howwow.schedulebot.schedule.service.lesson.utils;

import com.howwow.schedulebot.model.entity.Lesson;

import java.util.List;

public interface LessonFormatter {
    String format(List<Lesson> lessons);
}
