package com.howwow.schedulebot.parser.utils;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateUtils {

    public static boolean isEvenWeek() {
        int weekNumber = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfYear());
        return weekNumber % 2 == 0;
    }
}
