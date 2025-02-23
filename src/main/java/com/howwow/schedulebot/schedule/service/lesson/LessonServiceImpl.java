package com.howwow.schedulebot.schedule.service.lesson;

import com.howwow.schedulebot.model.entity.Lesson;
import com.howwow.schedulebot.parser.schedule.JsoupScheduleParser;
import com.howwow.schedulebot.schedule.service.lesson.utils.LessonFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final JsoupScheduleParser jsoupScheduleParser;
    private final LessonFormatter lessonFormatter;

    @Override
    public String getFormattedLessonsForGroup(String groupName) {
        log.info("Выполняется парсинг и форматирование расписания для группы: {}", groupName);
        List<Lesson> lessons = jsoupScheduleParser.parse(groupName);
        return lessonFormatter.format(lessons);
    }
}
