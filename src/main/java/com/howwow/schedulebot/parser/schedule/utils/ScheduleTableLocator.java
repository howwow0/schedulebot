package com.howwow.schedulebot.parser.schedule.utils;

import com.howwow.schedulebot.exception.schedule.ScheduleParseException;
import com.howwow.schedulebot.parser.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.format.TextStyle;
import java.util.Locale;

import static com.howwow.schedulebot.parser.schedule.JsoupScheduleParser.NO_LESSONS_TEXT;

@Component
@RequiredArgsConstructor
public class ScheduleTableLocator {
    private static final String HEADER_ROW_SELECTOR_TEMPLATE = "tr.headings:contains(%s)";


    public Elements locateCurrentDayScheduleRows(Document document) {
        String currentDay = getCurrentDayName();
        Element headerRow = document.selectFirst(
                String.format(HEADER_ROW_SELECTOR_TEMPLATE, currentDay)
        );

        if (headerRow == null || headerRow.parent() == null) {
            throw new ScheduleParseException("Schedule table header not found");
        }
        return extractRelevantRows(headerRow.parent().children(), headerRow.elementSiblingIndex());
    }

    private String getCurrentDayName() {
        return DateUtils.getCurrentDate()
                .getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.of("ru", "RU"));
    }

    private Elements extractRelevantRows(Elements rows, int headerIndex) {
        if (headerIndex + 1 >= rows.size()) {
            throw new ScheduleParseException("No lessons data after header");
        }

        Element firstDataRow = rows.get(headerIndex + 1);
        if (isNoLessonsRow(firstDataRow)) {
            return new Elements();
        }

        return collectLessonRows(rows, headerIndex);
    }

    private boolean isNoLessonsRow(Element row) {
        return row.select("td").getFirst().text().trim().equals(NO_LESSONS_TEXT);
    }

    private Elements collectLessonRows(Elements rows, int headerIndex) {
        Elements relevantRows = new Elements();
        for (int i = headerIndex + 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            if (row.hasClass("headings")) break;
            relevantRows.add(row);
        }
        return relevantRows;
    }
}