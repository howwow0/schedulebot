package com.howwow.schedulebot.schedule.utils.Impl;

import com.howwow.schedulebot.schedule.utils.DayOfWeekParser;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Component
public class DayOfWeekParserImpl implements DayOfWeekParser {
    @Override
    public Optional<DayOfWeek> parse(String text) {
        return Arrays.stream(DayOfWeek.values())
                .filter(day -> text.equalsIgnoreCase(day.getDisplayName(java.time.format.TextStyle.FULL, Locale.forLanguageTag("ru"))))
                .findFirst();
    }
}