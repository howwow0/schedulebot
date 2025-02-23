package com.howwow.schedulebot.parser.schedule;

import com.howwow.schedulebot.exception.schedule.ScheduleParseException;
import com.howwow.schedulebot.model.entity.GroupType;
import com.howwow.schedulebot.model.entity.Lesson;
import com.howwow.schedulebot.model.entity.WeekType;
import com.howwow.schedulebot.parser.Parser;
import com.howwow.schedulebot.parser.time.TimeParser;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class LessonRowParser implements Parser<Elements, Lesson> {
    private final TimeParser timeParser;

    public Lesson parse(Elements cells) {
        try {
            return Lesson.builder()
                    .numberLesson(parseLessonNumber(cells.get(0)))
                    .startTime(parseTime(cells.get(1), 0))
                    .endTime(parseTime(cells.get(1), 1))
                    .weekType(WeekType.fromString(cells.get(2).text().trim()))
                    .groupType(GroupType.fromString(cells.get(3).text().trim()))
                    .discipline(cells.get(4).text())
                    .professor(cells.get(5).text())
                    .room(cells.get(6).text())
                    .build();
        } catch (Exception e) {
            throw new ScheduleParseException("Error parsing lesson row: " + cells);
        }
    }

    private int parseLessonNumber(Element cell) {
        return Integer.parseInt(cell.text());
    }

    private LocalTime parseTime(Element cell, int index) {
        String[] timeParts = cell.text().split(" - ");
        return timeParser.parse(timeParts[index]);
    }
}