package com.howwow.schedulebot.schedule.utils;

import java.time.LocalTime;

public interface TimeParser {
    LocalTime parse(String time);
}