package com.howwow.schedulebot.schedule.service.Impl;

import com.howwow.schedulebot.telegram.dto.request.NotificationRequest;
import com.howwow.schedulebot.model.entities.ChatSettings;
import com.howwow.schedulebot.model.entities.Lesson;
import com.howwow.schedulebot.model.repository.ChatSettingsRepository;
import com.howwow.schedulebot.model.repository.LessonRepository;
import com.howwow.schedulebot.schedule.service.ScheduleService;
import com.howwow.schedulebot.schedule.utils.LessonFormatter;
import com.howwow.schedulebot.telegram.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ChatSettingsRepository chatSettingsRepository;
    private final LessonRepository lessonRepository;
    private final NotificationService notificationService;
    private final LessonFormatter lessonFormatter;

    @Override
    @Scheduled(cron = "0 0/30 * * * *")
    public void sendSchedule() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        Map<String, List<ChatSettings>> chatsByGroups = chatSettingsRepository.findGroupingByGroupName();
        chatsByGroups.keySet().forEach(groupName -> {
            List<Lesson> groupLessons = lessonRepository.findByGroupName(groupName);
            List<ChatSettings> chats = chatsByGroups.get(groupName);
            chats.forEach(chat -> {
                if (chat.getDeliveryTime() != null && chat.getDeliveryTime().equals(now))
                    notificationService.notify(NotificationRequest.builder()
                            .chatId(chat.getChatId())
                            .messageThreadId(chat.getMessageThreadId())
                            .text(lessonFormatter.format(groupLessons))
                            .build());
            });
        });
    }
}
