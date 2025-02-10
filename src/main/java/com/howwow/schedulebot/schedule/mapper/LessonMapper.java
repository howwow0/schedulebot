package com.howwow.schedulebot.schedule.mapper;

import com.howwow.schedulebot.schedule.dto.request.LessonRequest;
import com.howwow.schedulebot.schedule.dto.response.LessonResponse;
import com.howwow.schedulebot.model.entities.Lesson;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LessonMapper {

    public LessonResponse asResponse(Lesson lesson) {
        return LessonResponse.builder().
                groupName(lesson.getGroupName())
                .startTime(lesson.getStartTime())
                .endTime(lesson.getEndTime())
                .numberLesson(lesson.getNumberLesson())
                .room(lesson.getRoom())
                .groupType(lesson.getGroupType())
                .discipline(lesson.getDiscipline())
                .professor(lesson.getProfessor())
                .build();
    }

    public Lesson asEntity(LessonRequest lessonRequest) {
        return Lesson.builder()
                .id(lessonRequest.groupName() + ":" + lessonRequest.numberLesson())
                .groupName(lessonRequest.groupName())
                .startTime(lessonRequest.startTime())
                .endTime(lessonRequest.endTime())
                .numberLesson(lessonRequest.numberLesson())
                .room(lessonRequest.room())
                .groupType(lessonRequest.groupType())
                .discipline(lessonRequest.discipline())
                .professor(lessonRequest.professor())
                .build();
    }

    public List<LessonResponse> asListResponse(List<Lesson> lessonList) {
        List<LessonResponse> lessonResponses = new ArrayList<>();
        for (Lesson lesson : lessonList) {
            lessonResponses.add(asResponse(lesson));
        }
        return lessonResponses;
    }
}
