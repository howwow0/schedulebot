package com.howwow.schedulebot.parser.schedule.utils;

import com.howwow.schedulebot.model.entity.Lesson;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LessonMerger {
    public void mergeOrAdd(List<Lesson> lessons, Lesson newLesson) {
        lessons.stream()
                .filter(existingLesson -> existingLesson.getNumberLesson() == newLesson.getNumberLesson() &&
                        existingLesson.getWeekType() == newLesson.getWeekType() &&
                        existingLesson.getGroupType() == newLesson.getGroupType()&&
                        existingLesson.getStartTime().equals(newLesson.getStartTime())&&
                        existingLesson.getEndTime().equals(newLesson.getEndTime())&&
                        existingLesson.getDiscipline().equals(newLesson.getDiscipline())&&
                        existingLesson.getRoom().equals(newLesson.getRoom()))
                .findFirst()
                .ifPresentOrElse(
                        existing -> existing.setProfessor(
                                existing.getProfessor() + ", " + newLesson.getProfessor()
                        ),
                        () -> lessons.add(newLesson)
                );
    }
}