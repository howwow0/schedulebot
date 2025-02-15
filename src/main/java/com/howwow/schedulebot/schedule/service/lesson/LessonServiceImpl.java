package com.howwow.schedulebot.schedule.service.lesson;

import com.howwow.schedulebot.model.entity.Lesson;
import com.howwow.schedulebot.parser.schedule.JsoupScheduleParser;
import com.howwow.schedulebot.schedule.service.lesson.dto.request.FindLessonByGroupNameRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final JsoupScheduleParser jsoupScheduleParser;

    @Override
    @Cacheable(value = "lessonsCache", key = "#findLessonByGroupNameRequest.groupName")
    public List<Lesson> findLessonsByGroupName(FindLessonByGroupNameRequest findLessonByGroupNameRequest) {
        log.info("Выполняется парсинг расписания для группы: {}", findLessonByGroupNameRequest.groupName());
        return jsoupScheduleParser.parse(findLessonByGroupNameRequest.groupName());
    }
}
