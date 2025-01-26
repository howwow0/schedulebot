package com.howwow.schedulebot.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "schedules", indexes = {
        @Index(name = "idx_group_day", columnList = "group_name, day_of_week")
})
public class Schedule extends AbstractEntity{

    @Column(nullable = false, name = "group_name")
    private String groupName;

    @Column(nullable = false, name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

}
