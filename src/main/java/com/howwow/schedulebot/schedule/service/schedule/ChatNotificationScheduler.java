package com.howwow.schedulebot.schedule.service.schedule;

import com.howwow.schedulebot.model.entity.ChatSettings;
import com.howwow.schedulebot.repository.ChatSettingsRepository;
import com.howwow.schedulebot.schedule.service.lesson.LessonService;
import com.howwow.schedulebot.telegram.dto.request.NotificationRequest;
import com.howwow.schedulebot.telegram.service.NotificationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatNotificationScheduler {

    private final TaskScheduler taskScheduler;
    private final ChatSettingsRepository chatSettingsRepository;
    private final NotificationService notificationService;
    private final LessonService lessonService;

    private final Map<Long, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        chatSettingsRepository.findAllActiveChatSettings().forEach(this::scheduleIfValid);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChatSettingsEvent(ChatSettingsChangedEvent event) {
        ChatSettings settings = event.getSettings();

        if (chatSettingsRepository.existsById(settings.getId())) {
            reschedule(settings);
        } else {
            cancel(settings.getChatId());
        }
    }

    private void scheduleIfValid(ChatSettings settings) {
        if (isValid(settings)) {
            schedule(settings);
        }
    }

    private void schedule(ChatSettings settings) {
        cancel(settings.getChatId());

        String cron = String.format("0 %d %d * * *",
                settings.getDeliveryTime().getMinute(),
                settings.getDeliveryTime().getHour());

        ScheduledFuture<?> task = taskScheduler.schedule(
                () -> sendNotification(settings),
                new CronTrigger(cron)
        );

        tasks.put(settings.getChatId(), task);
        log.info("Запланирована отправка для чата {}", settings.getChatId());
    }

    private void reschedule(ChatSettings settings) {
        cancel(settings.getChatId());

        if (isValid(settings)) {
            schedule(settings);
        }
    }

    private void sendNotification(ChatSettings settings) {
        try {
            notificationService.notify(NotificationRequest.builder()
                            .chatId(settings.getChatId())
                            .messageThreadId(settings.getMessageThreadId())
                            .text(lessonService.getFormattedLessonsForGroup(settings.getGroupName()))
                    .build()
            );
        } catch (Exception e) {
            log.error("Отправка неудачна для чата {}", settings.getChatId(), e);
            cancel(settings.getChatId());
        }
    }

    private void cancel(Long chatId) {
        ScheduledFuture<?> task = tasks.remove(chatId);
        if (task != null) {
            task.cancel(false);
            log.info("Отменена отправка для чата {}", chatId);
        }
    }

    private boolean isValid(ChatSettings settings) {
        return settings.getIsActive()
                && settings.getDeliveryTime() != null
                && settings.getGroupName() != null
                && !settings.getGroupName().isBlank();
    }
}