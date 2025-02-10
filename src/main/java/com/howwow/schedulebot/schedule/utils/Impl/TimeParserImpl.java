package com.howwow.schedulebot.schedule.utils.Impl;

import com.howwow.schedulebot.schedule.utils.TimeParser;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class TimeParserImpl implements TimeParser {
    private static final String TIME_FORMAT_24H = "HH:mm";
    private static final String TIME_FORMAT_24H_ONE_H = "H:mm";

    @Override
    public LocalTime parse(String time) {
        try {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern(TIME_FORMAT_24H));
        } catch (DateTimeParseException e) {
            try {
                return LocalTime.parse(time, DateTimeFormatter.ofPattern(TIME_FORMAT_24H_ONE_H));
            } catch (DateTimeParseException e2) {
                return null;
            }
        }
    }
}