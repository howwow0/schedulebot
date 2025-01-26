package com.howwow.schedulebot.chat.service;

import com.howwow.schedulebot.chat.dto.request.*;
import com.howwow.schedulebot.chat.dto.response.*;
import com.howwow.schedulebot.exception.AlreadyExistsException;
import com.howwow.schedulebot.exception.NotFoundException;

public interface ChatSettingsService {
    CreatedChatResponse create(CreateChatSettingsRequest createChatSettingsRequest) throws AlreadyExistsException;
    FoundedChatResponse findByChatId(FindChatRequest chatRequest) throws NotFoundException;
    UpdatedChatResponse updateMessageThreadId(UpdateChatSettingsRequest updateChatSettingsRequest) throws NotFoundException;
    UpdatedGroupNameChatResponse updateGroupName(UpdateGroupNameChatSettingsRequest updateGroupNameRequest) throws NotFoundException;
    UpdatedDeliveryTimeResponse updateDeliveryTime(UpdateDeliveryTimeChatSettingsRequest updateDeliveryTimeChatSettingsRequest) throws NotFoundException;
}
