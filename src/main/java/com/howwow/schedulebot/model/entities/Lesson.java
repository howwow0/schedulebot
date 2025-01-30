package com.howwow.schedulebot.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@RedisHash("Lesson")
public class Lesson {

    @Id
    private String id;

    @Indexed
    private String groupName;

    private int numberLesson;

    private LocalTime startTime;

    private LocalTime endTime;

    private GroupType groupType;

    private String discipline;

    private String professor;

    private String room;

}
