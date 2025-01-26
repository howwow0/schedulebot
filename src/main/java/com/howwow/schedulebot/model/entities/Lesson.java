package com.howwow.schedulebot.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "lessons")
public class Lesson extends AbstractEntity {

    @Column(name = "number_lesson", nullable = false)
    private int numberLesson;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "week_type", nullable = false)
    private WeekType weekType;

    @Enumerated(EnumType.STRING)
    @Column(name = "group_type", nullable = false)
    private GroupType groupType;

    @Column(nullable = false)
    private String discipline;

    @Column(nullable = false)
    private String professor;

    @Column(nullable = false)
    private String room;

    @ManyToOne()
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;
}
