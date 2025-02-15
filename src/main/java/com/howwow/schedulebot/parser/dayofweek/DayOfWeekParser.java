package com.howwow.schedulebot.parser.dayofweek;

import com.howwow.schedulebot.parser.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
@Slf4j
public class DayOfWeekParser implements Parser<String, DayOfWeek> {
    private static final Map<String, DayOfWeek> dayOfWeekMap = new HashMap<>();

    static {
        for (DayOfWeek day : DayOfWeek.values()) {
            String russianName = day.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru")).toLowerCase();
            dayOfWeekMap.put(russianName, day);
        }
    }

    @Override
    public DayOfWeek parse(String text) {
        log.info("Начало парсинга дня недели: '{}'", text);

        if (text == null || text.isBlank()) {
            log.error("Входной текст для парсинга дня недели пустой или null");
            throw new IllegalArgumentException("День недели не может быть пустым");
        }

        DayOfWeek day = dayOfWeekMap.get(text.toLowerCase());
        if (day == null) {
            log.error("Не удалось определить день недели для текста: '{}'", text);
            throw new IllegalArgumentException("Неправильное название дня недели: " + text);
        }

        log.info("Успешно распарсено: '{}' -> '{}'", text, day);
        return day;
    }
}
