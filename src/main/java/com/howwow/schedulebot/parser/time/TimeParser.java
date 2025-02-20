package com.howwow.schedulebot.parser.time;

import com.howwow.schedulebot.exception.chat.ChatDateTimeParseException;
import com.howwow.schedulebot.parser.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
@Slf4j
public class TimeParser implements Parser<String, LocalTime> {

    private static final List<String> TIME_FORMATS = List.of("HH:mm", "H:mm");

    @Override
    public LocalTime parse(String time) {
        log.info("Начало парсинга времени: '{}'", time);

        if (time == null || time.isBlank()) {
            log.error("Входное значение времени пустое или null");
            throw new ChatDateTimeParseException("Входное значение времени пустое или null", time, 0);
        }

        for (String format : TIME_FORMATS) {
            log.debug("Пробуем распарсить время '{}' с форматом '{}'", time, format);
            try {
                LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern(format));
                log.info("Время '{}' успешно распарсено в '{}'", time, parsedTime);
                return parsedTime;
            } catch (DateTimeParseException ignored) {
                log.warn("Не удалось распарсить время '{}' с форматом '{}'", time, format);
            }
        }

        log.error("Неверный формат времени: '{}'", time);
        throw new ChatDateTimeParseException("Неверный формат времени", time, 0);
    }
}
