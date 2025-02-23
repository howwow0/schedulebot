package com.howwow.schedulebot.schedule.service.schedule;

import com.howwow.schedulebot.model.entity.ChatSettings;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatSettingsJpaListener {
    private final ApplicationEventPublisher eventPublisher;

    @PostPersist
    @PostUpdate
    public void postPersistOrUpdate(ChatSettings settings) {
        eventPublisher.publishEvent(new ChatSettingsChangedEvent(this, settings));
    }

    @PostRemove
    public void postRemove(ChatSettings settings) {
        eventPublisher.publishEvent(new ChatSettingsChangedEvent(this, settings));
    }
}