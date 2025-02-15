package com.howwow.schedulebot.schedule.service.lesson.utils;

import com.howwow.schedulebot.model.entity.GroupType;
import com.howwow.schedulebot.model.entity.Lesson;
import com.howwow.schedulebot.telegram.utils.MarkdownV2Escaper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class LessonFormatterImpl implements LessonFormatter {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public String format(List<Lesson> lessons) {
        log.info("Начинаем форматирование {} занятий", lessons == null ? 0 : lessons.size());

        if (lessons == null || lessons.isEmpty()) {
            log.info("Сегодня занятий нет.");
            return "📅 *Сегодня занятий нет*";
        }

        String result = "📅 *Расписание занятий:*" + "\n\n" +
                lessons.stream()
                        .map(this::formatLesson)
                        .collect(Collectors.joining("\n\n"));

        log.info("Форматирование завершено.");
        return result;
    }

    private String formatLesson(Lesson lesson) {
        String groupInfo = lesson.getGroupType() == GroupType.BOTH
                ? ""
                : "\n   👥 " + MarkdownV2Escaper.escape(GroupType.toString(lesson.getGroupType()));

        String formattedLesson = String.format(
                """
                🎯 *%d пара \\(%s \\- %s\\)\\:*
                   📚 %s
                   👨‍🏫 %s
                   🚪 Аудитория %s%s""",
                lesson.getNumberLesson(),
                MarkdownV2Escaper.escape(lesson.getStartTime().format(TIME_FORMATTER)),
                MarkdownV2Escaper.escape(lesson.getEndTime().format(TIME_FORMATTER)),
                MarkdownV2Escaper.escape(lesson.getDiscipline()),
                MarkdownV2Escaper.escape(lesson.getProfessor()),
                MarkdownV2Escaper.escape(lesson.getRoom()),
                groupInfo
        );

        log.debug("Отформатировано занятие: {}", formattedLesson);
        return formattedLesson;
    }
}
