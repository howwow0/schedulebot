package com.howwow.schedulebot.schedule.service.Impl;

import com.howwow.schedulebot.schedule.dto.response.LessonResponse;
import com.howwow.schedulebot.schedule.mapper.LessonMapper;
import com.howwow.schedulebot.schedule.service.LessonService;
import com.howwow.schedulebot.model.entities.Lesson;
import com.howwow.schedulebot.model.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    @Override
    public List<LessonResponse> findLessonsByGroupName(String groupName) {
        return lessonMapper.asListResponse(lessonRepository.findByGroupName(groupName));
    }

    @Override
    public void updateLessonsForGroup(String groupName, List<Lesson> newLessons) {

    }
}
