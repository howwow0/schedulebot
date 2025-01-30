package com.howwow.schedulebot.schedule.utils.Impl;

import com.howwow.schedulebot.model.entities.Lesson;
import com.howwow.schedulebot.schedule.utils.LessonFormatter;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LessonFormatterImpl implements LessonFormatter {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public String format(List<Lesson> lessons) {
        if (lessons == null || lessons.isEmpty()) {
            return "📅 *Расписание не найдено.*";
        }

        return "📅 *Расписание занятий:*\n\n" +
                lessons.stream()
                        .map(lesson -> String.format(
                                """
                                        🎯 *Урок %d:*
                                           ⏰ *Время:* %s - %s
                                           📚 *Дисциплина:* %s
                                           👨‍🏫 *Преподаватель:* %s
                                           🚪 *Аудитория:* %s""",
                                lesson.getNumberLesson(),
                                lesson.getStartTime().format(TIME_FORMATTER),
                                lesson.getEndTime().format(TIME_FORMATTER),
                                lesson.getDiscipline(),
                                lesson.getProfessor(),
                                lesson.getRoom()))
                        .collect(Collectors.joining("\n\n"));
    }
}
