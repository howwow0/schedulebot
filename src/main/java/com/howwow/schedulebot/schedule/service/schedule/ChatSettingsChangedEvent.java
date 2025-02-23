package com.howwow.schedulebot.schedule.service.schedule;

import com.howwow.schedulebot.model.entity.ChatSettings;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ChatSettingsChangedEvent extends ApplicationEvent {
    private final ChatSettings settings;

    public ChatSettingsChangedEvent(Object source, ChatSettings settings) {
        super(source);
        this.settings = settings;
    }

}