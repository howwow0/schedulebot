package com.howwow.schedulebot.schedule.service.lesson;

import com.howwow.schedulebot.model.entity.Lesson;
import com.howwow.schedulebot.schedule.service.lesson.dto.request.FindLessonByGroupNameRequest;

import java.util.List;

public interface LessonService {
    List<Lesson> findLessonsByGroupName(FindLessonByGroupNameRequest findLessonByGroupNameRequest);
}
