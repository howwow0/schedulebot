package com.howwow.schedulebot.chat.service;

import com.howwow.schedulebot.chat.dto.request.*;
import com.howwow.schedulebot.chat.dto.response.*;
import com.howwow.schedulebot.exception.AlreadyExistsException;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.exception.ValidationException;

public interface ChatSettingsService {
    Long create(Long chatId) throws AlreadyExistsException;
    FoundedChatResponse findByChatId(Long chatId) throws NotFoundException;
    void updateMessageThreadId(Long chatId, Integer messageThreadId) throws NotFoundException;
    UpdatedGroupNameChatResponse updateGroupName(UpdateGroupNameChatSettingsRequest updateGroupNameRequest) throws NotFoundException;
    UpdatedDeliveryTimeResponse updateDeliveryTime(UpdateDeliveryTimeChatSettingsRequest updateDeliveryTimeChatSettingsRequest) throws NotFoundException, ValidationException;
    void removeDeliveryTime(Long chatId) throws NotFoundException;
}
