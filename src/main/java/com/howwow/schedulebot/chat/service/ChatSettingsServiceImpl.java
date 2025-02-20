package com.howwow.schedulebot.chat.service;

import com.howwow.schedulebot.chat.dto.request.*;
import com.howwow.schedulebot.chat.dto.response.*;
import com.howwow.schedulebot.exception.chat.ChatAlreadyExistsException;
import com.howwow.schedulebot.exception.chat.ChatNotFoundException;
import com.howwow.schedulebot.exception.chat.GroupNotFoundException;
import com.howwow.schedulebot.model.entity.ChatSettings;
import com.howwow.schedulebot.repository.ChatSettingsRepository;
import com.howwow.schedulebot.chat.mapper.ChatSettingsMapper;
import com.howwow.schedulebot.chat.utils.SettingsFormatter;
import com.howwow.schedulebot.schedule.service.group.GroupService;
import com.howwow.schedulebot.schedule.service.group.dto.request.GroupCheckRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatSettingsServiceImpl implements ChatSettingsService {

    private final ChatSettingsRepository chatSettingsRepository;
    private final ChatSettingsMapper chatSettingsMapper;
    private final SettingsFormatter settingsFormatter;
    private final GroupService groupService;

    @Override
    @Transactional
    @Modifying
    public CreatedChatResponse create(ChatIdRequest chatIdRequest) throws ChatAlreadyExistsException {
        if (chatSettingsRepository.existsChatSettingsByChatId(chatIdRequest.chatId())) {
            throw new ChatAlreadyExistsException(chatIdRequest.chatId());
        }

        ChatSettings chatSettings = ChatSettings.builder()
                .chatId(chatIdRequest.chatId())
                .build();

        chatSettingsRepository.save(chatSettings);
        return chatSettingsMapper.asCreatedChatResponse(chatSettings);
    }

    @Override
    public FoundedChatResponse findByChatId(Long chatId) throws ChatNotFoundException {
        return chatSettingsRepository.findChatSettingsByChatId(chatId)
                .map(chatSettingsMapper::asFoundChatResponse)
                .orElseThrow(() -> new ChatNotFoundException(chatId));
    }

    @Override
    @Transactional
    @Modifying
    public UpdatedMessageThreadIdResponse updateMessageThreadId(UpdateMessageThreadIdRequest updateMessageThreadIdRequest) throws ChatNotFoundException {
        ChatSettings chatSettings = chatSettingsRepository.findChatSettingsByChatId(updateMessageThreadIdRequest.chatId())
                .orElseThrow(() -> new ChatNotFoundException(updateMessageThreadIdRequest.chatId()));

        chatSettingsMapper.updateMessageThreadId(chatSettings, updateMessageThreadIdRequest.messageThreadId());

        return chatSettingsMapper.asUpdatedMessageThreadIdResponse(chatSettings);
    }

    @Override
    @Transactional
    @Modifying
    public UpdatedGroupNameChatResponse updateGroupName(UpdateGroupNameChatSettingsRequest updateGroupNameRequest) throws ChatNotFoundException, GroupNotFoundException {
        if(!groupService.isGroupExist(GroupCheckRequest.builder()
                .groupName(updateGroupNameRequest.groupName())
                .build())) {
            throw new GroupNotFoundException(updateGroupNameRequest.groupName());
        }

        ChatSettings chatSettings = chatSettingsRepository.findChatSettingsByChatId(updateGroupNameRequest.chatId())
                .orElseThrow(() -> new ChatNotFoundException(updateGroupNameRequest.chatId()));

        chatSettingsMapper.updateGroupName(chatSettings, updateGroupNameRequest.groupName());

        return chatSettingsMapper.asUpdatedGroupNameResponse(chatSettings);
    }

    @Override
    @Transactional
    @Modifying
    public UpdatedDeliveryTimeResponse updateDeliveryTime(UpdateDeliveryTimeChatSettingsRequest updateDeliveryTimeChatSettingsRequest) throws ChatNotFoundException {
        ChatSettings chatSettings = chatSettingsRepository.findChatSettingsByChatId(updateDeliveryTimeChatSettingsRequest.chatId())
                .orElseThrow(() -> new ChatNotFoundException(updateDeliveryTimeChatSettingsRequest.chatId()));

        chatSettingsMapper.updateDeliveryTime(chatSettings, updateDeliveryTimeChatSettingsRequest.deliveryTime());

        return chatSettingsMapper.asUpdatedDeliveryTimeResponse(chatSettings);
    }

    @Override
    @Transactional
    @Modifying
    public UpdatedIsActiveStatusResponse toggleIsActive(Long chatId) throws ChatNotFoundException {
        ChatSettings chatSettings = chatSettingsRepository.findChatSettingsByChatId(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));

        chatSettingsMapper.toggleIsActive(chatSettings);

        return chatSettingsMapper.asUpdatedIsActiveStatusResponse(chatSettings);
    }

    @Override
    public String getFormattedChatSettings(Long chatId) throws ChatNotFoundException {
        return chatSettingsMapper.format(chatSettingsRepository.findChatSettingsByChatId(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId)), settingsFormatter);
    }
}
