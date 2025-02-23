package com.howwow.schedulebot.schedule.service.lesson;

import com.howwow.schedulebot.config.MessageTemplates;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public interface LessonService {
    String getFormattedLessonsForGroup(@NotBlank(message = MessageTemplates.GROUP_NAME_NOT_FOUND_ERROR) String groupName);
}
