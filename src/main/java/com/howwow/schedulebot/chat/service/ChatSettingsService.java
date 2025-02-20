package com.howwow.schedulebot.chat.service;

import com.howwow.schedulebot.chat.dto.request.*;
import com.howwow.schedulebot.chat.dto.response.*;
import com.howwow.schedulebot.exception.chat.ChatAlreadyExistsException;
import com.howwow.schedulebot.exception.chat.ChatNotFoundException;
import com.howwow.schedulebot.exception.chat.GroupNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ChatSettingsService {
    CreatedChatResponse create(@Valid  ChatIdRequest chatIdRequest) throws ChatAlreadyExistsException;
    FoundedChatResponse findByChatId(@NotNull Long chatId) throws ChatNotFoundException;
    UpdatedMessageThreadIdResponse updateMessageThreadId(@Valid UpdateMessageThreadIdRequest updateMessageThreadIdRequest) throws ChatNotFoundException;
    UpdatedGroupNameChatResponse updateGroupName(@Valid UpdateGroupNameChatSettingsRequest updateGroupNameRequest) throws ChatNotFoundException, GroupNotFoundException;
    UpdatedDeliveryTimeResponse updateDeliveryTime(@Valid UpdateDeliveryTimeChatSettingsRequest updateDeliveryTimeChatSettingsRequest) throws ChatNotFoundException;
    UpdatedIsActiveStatusResponse toggleIsActive(@NotNull Long chatId) throws ChatNotFoundException;
    String getFormattedChatSettings(@NotNull Long chatId) throws ChatNotFoundException;
}
