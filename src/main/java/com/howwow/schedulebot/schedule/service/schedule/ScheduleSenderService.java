package com.howwow.schedulebot.schedule.service.schedule;

import com.howwow.schedulebot.model.entity.Lesson;
import com.howwow.schedulebot.schedule.service.lesson.LessonService;
import com.howwow.schedulebot.schedule.service.lesson.dto.request.FindLessonByGroupNameRequest;
import com.howwow.schedulebot.telegram.dto.request.NotificationRequest;
import com.howwow.schedulebot.model.entity.ChatSettings;
import com.howwow.schedulebot.repository.ChatSettingsRepository;
import com.howwow.schedulebot.schedule.service.lesson.utils.LessonFormatter;
import com.howwow.schedulebot.telegram.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleSenderService {

    private final ChatSettingsRepository chatSettingsRepository;
    private final LessonService lessonService;
    private final NotificationService notificationService;
    private final LessonFormatter lessonFormatter;
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Scheduled(cron = "0 0/30 * * * *")
    private void sendSchedule() {
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        Map<String, List<ChatSettings>> chatsByGroups = chatSettingsRepository.findGroupingByGroupName();

        chatsByGroups.keySet().parallelStream().forEach(groupName -> {
            if (groupName != null) {
                List<Lesson> groupLessons = lessonService.findLessonsByGroupName(
                        FindLessonByGroupNameRequest.builder()
                        .groupName(groupName)
                        .build());
                List<ChatSettings> chats = chatsByGroups.get(groupName);

                chats.parallelStream().forEach(chat -> {
                    if (chat.getGroupName() != null && chat.getDeliveryTime() != null && chat.getDeliveryTime().equals(now)) {
                        log.info("Отправка расписания для группы '{}' в чат '{}'", groupName, chat.getChatId());
                        sendLessonsForChat(chat, groupLessons);
                    }
                });
            }
        });

    }

    private void sendLessonsForChat(ChatSettings chat, List<Lesson> lessons) {
        executor.submit(() -> {
            try {
                notificationService.notify(NotificationRequest.builder()
                        .chatId(chat.getChatId())
                        .messageThreadId(chat.getMessageThreadId())
                        .text(lessonFormatter.format(lessons))
                        .build());
                log.info("Уведомление успешно отправлено для чата '{}'", chat.getChatId());
            } catch (Exception e) {
                log.error("Ошибка при отправке уведомления для чата '{}': {}", chat.getChatId(), e.getMessage(), e);
            }
        });
    }
}
