package com.howwow.schedulebot.parser.schedule;
import com.howwow.schedulebot.exception.schedule.ScheduleParseException;
import com.howwow.schedulebot.model.entity.Lesson;
import com.howwow.schedulebot.model.entity.WeekType;
import com.howwow.schedulebot.parser.Parser;
import com.howwow.schedulebot.parser.jsoup.JsoupDocumentParser;
import com.howwow.schedulebot.parser.schedule.utils.LessonMerger;
import com.howwow.schedulebot.parser.schedule.utils.ScheduleTableLocator;
import com.howwow.schedulebot.parser.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsoupScheduleParser implements Parser<String, List<Lesson>> {

    private static final String SCHEDULE_URL_TEMPLATE = "https://ios.biti.mephi.ru/raspisanie/index.php?gr=%s";
    public static final String NO_LESSONS_TEXT = "Нет пар";
    private static final int MIN_CELLS_IN_ROW = 7;

    private final JsoupDocumentParser documentFetcher;
    private final ScheduleTableLocator tableLocator;
    private final LessonRowParser lessonRowParser;
    private final LessonMerger lessonMerger;

    @Override
    @Cacheable(
            value = "scheduleCache",
            key = "#groupName + '_' + T(java.time.LocalDate).now()",
            unless = "#result.isEmpty()"
    )
    public List<Lesson> parse(String groupName) {
        log.info("Starting schedule parsing for group: {}", groupName);

        Document document = fetchScheduleDocument(groupName);
        Elements scheduleRows = locateScheduleRows(document);

        if (scheduleRows.isEmpty()) {
            return Collections.emptyList();
        }

        return processScheduleRows(scheduleRows);
    }

    private Document fetchScheduleDocument(String groupName) {
        String url = String.format(SCHEDULE_URL_TEMPLATE, groupName);
        return documentFetcher.parse(url);
    }

    private Elements locateScheduleRows(Document document) {
        return tableLocator.locateCurrentDayScheduleRows(document);
    }

    private List<Lesson> processScheduleRows(Elements rows) {
        List<Lesson> lessons = new ArrayList<>();

        for (Element row : rows) {
            if (row.hasClass("headings")) {
                continue;
            }

            Lesson lesson = parseLessonRow(row);
            if (shouldIncludeLesson(lesson)) {
                lessonMerger.mergeOrAdd(lessons, lesson);
            }
        }

        return lessons;
    }

    private Lesson parseLessonRow(Element row) {
        Elements cells = row.select("td");
        validateRowCells(cells);
        return lessonRowParser.parse(cells);
    }

    private void validateRowCells(Elements cells) {
        if (cells.size() < MIN_CELLS_IN_ROW) {
            throw new ScheduleParseException(
                    "Invalid row structure. Expected at least " + MIN_CELLS_IN_ROW +
                            " cells, got " + cells.size()
            );
        }
    }

    private boolean shouldIncludeLesson(Lesson lesson) {
        WeekType currentWeekType = DateUtils.getCurrentWeekType();
        return lesson.getWeekType() == WeekType.BOTH ||
                lesson.getWeekType() == currentWeekType;
    }
}
