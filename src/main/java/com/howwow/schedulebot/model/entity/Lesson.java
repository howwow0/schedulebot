package com.howwow.schedulebot.model.entity;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    private String groupName;
    private int numberLesson;
    private LocalTime startTime;
    private LocalTime endTime;
    private GroupType groupType;
    private WeekType weekType;
    private String discipline;
    private String professor;
    private String room;
}
