package com.howwow.schedulebot.schedule.service;

import com.howwow.schedulebot.schedule.dto.response.LessonResponse;
import com.howwow.schedulebot.model.entities.Lesson;

import java.util.List;

public interface LessonService {
    List<LessonResponse> findLessonsByGroupName(String groupName);
    void updateLessonsForGroup(String groupName, List<Lesson> newLessons);
}
