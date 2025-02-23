package com.howwow.schedulebot.parser.utils;

import com.howwow.schedulebot.model.entity.WeekType;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateUtils {

    public static boolean isEvenWeek() {
        int weekNumber = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfYear());
        return weekNumber % 2 == 0;
    }

    public static WeekType getCurrentWeekType() {
        return isEvenWeek() ? WeekType.EVEN : WeekType.ODD;
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
