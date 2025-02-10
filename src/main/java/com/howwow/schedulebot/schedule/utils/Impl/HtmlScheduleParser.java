package com.howwow.schedulebot.schedule.utils.Impl;

import com.howwow.schedulebot.model.entities.*;
import com.howwow.schedulebot.schedule.utils.DayOfWeekParser;
import com.howwow.schedulebot.schedule.utils.Fetcher;
import com.howwow.schedulebot.schedule.utils.ScheduleParser;
import com.howwow.schedulebot.schedule.utils.TimeParser;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Component
public class HtmlScheduleParser implements ScheduleParser {

    private static final String NO_LESSONS_TEXT = "Нет пар";

    private final TimeParser timeParser;
    private final DayOfWeekParser dayOfWeekParser;
    private final Fetcher fetcher;

    @Override
    public List<Lesson> parse(String groupName) {
        List<Lesson> lessons = new ArrayList<>();
        try {
            String url = "https://ios.biti.mephi.ru/raspisanie/index.php?gr=" + groupName;
            String html = fetcher.fetch(url);
            Document document = Jsoup.parse(html);

            DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();

            WeekType currentWeekType = isEvenWeek() ? WeekType.EVEN : WeekType.ODD;

            Elements rows = document.select("table tbody tr");
            DayOfWeek dayOfWeek = null;

            for (Element row : rows) {
                Elements headers = row.select("th.column-title");
                if (!headers.isEmpty()) {
                    String dayText = headers.getFirst().text();
                    dayOfWeek = dayOfWeekParser.parse(dayText).orElse(null);
                } else if (dayOfWeek != null && dayOfWeek == currentDayOfWeek) {
                    Elements cells = row.select("td");
                    if (cells.size() >= 7) {
                        if (cells.getFirst().text().equals(NO_LESSONS_TEXT)) {
                            return lessons;
                        }

                        Lesson lesson = parseLesson(cells);
                        if (lesson != null && isLessonRelevant(lesson, currentWeekType)) {
                            lessons.add(lesson);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lessons;
    }

    private Lesson parseLesson(Elements cells) {
        if (cells.get(0).text().equals(NO_LESSONS_TEXT)) {
            return null;
        }

        String[] timeRange = cells.get(1).text().split(" - ");

        return Lesson.builder()
                .numberLesson(Integer.parseInt(cells.get(0).text()))
                .startTime(timeParser.parse(timeRange[0]))
                .endTime(timeParser.parse(timeRange[1]))
                .weekType(WeekType.fromString(cells.get(2).text().trim()))
                .groupType(GroupType.fromString(cells.get(3).text().trim()))
                .discipline(cells.get(4).text())
                .professor(cells.get(5).text())
                .room(cells.get(6).text())
                .build();
    }

    private boolean isLessonRelevant(Lesson lesson, WeekType currentWeekType) {
        return (lesson.getWeekType() == WeekType.BOTH || lesson.getWeekType() == currentWeekType);
    }

    private boolean isEvenWeek() {
        LocalDate today = LocalDate.now();
        int weekNumber = today.get(WeekFields.of(Locale.getDefault()).weekOfYear());
        return weekNumber % 2 == 0;
    }
}