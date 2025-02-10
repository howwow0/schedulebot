package com.howwow.schedulebot.schedule.utils;

import java.time.DayOfWeek;
import java.util.Optional;

public interface DayOfWeekParser {
    Optional<DayOfWeek> parse(String text);
}
