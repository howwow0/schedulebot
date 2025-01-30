package com.howwow.schedulebot.telegram.service;

import com.howwow.schedulebot.telegram.dto.request.NotificationRequest;

public interface NotificationService {
    void notify(NotificationRequest notificationRequest);
}
