package com.howwow.schedulebot.chat.service;

import com.howwow.schedulebot.chat.dto.request.*;
import com.howwow.schedulebot.chat.dto.response.*;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.chat.mapper.ChatSettingsMapper;
import com.howwow.schedulebot.exception.AlreadyExistsException;
import com.howwow.schedulebot.exception.ValidationException;
import com.howwow.schedulebot.model.entity.ChatSettings;
import com.howwow.schedulebot.repository.ChatSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ChatSettingsServiceImpl implements ChatSettingsService {

    private final ChatSettingsRepository chatSettingsRepository;
    private final ChatSettingsMapper chatSettingsMapper;


    @Transactional
    @Modifying
    @Override
    public Long create(Long chatId) throws AlreadyExistsException {
        ChatSettings chatSettings = ChatSettings.builder()
                .chatId(chatId)
                .build();

        if (chatSettingsRepository.existsByChatId(chatSettings.getChatId()))
            throw new AlreadyExistsException("Chat with id " + chatSettings.getChatId() + " already exists");

        chatSettingsRepository.save(chatSettings);

        return chatSettings.getChatId();
    }

    @Override
    public FoundedChatResponse findByChatId(Long chatId) throws NotFoundException {
        ChatSettings chatSettings = chatSettingsRepository.findByChatId(chatId);

        if(chatSettings == null) {
            throw new NotFoundException("Chat with id " + chatId + " not found");
        }

        return chatSettingsMapper.asFoundChatResponse(chatSettings);
    }

    @Override
    @Transactional
    @Modifying
    public void updateMessageThreadId(Long chatId, Integer messageThreadId) throws NotFoundException {

        ChatSettings chatSettings = chatSettingsRepository.findByChatId(chatId);

        if(chatSettings == null) {
            throw new NotFoundException("Chat with id " + chatId + " not found");
        }

        chatSettingsMapper.updateMessageThreadId(chatSettings, messageThreadId);

    }

    @Override
    @Transactional
    @Modifying
    public UpdatedGroupNameChatResponse updateGroupName(UpdateGroupNameChatSettingsRequest updateGroupNameRequest) throws NotFoundException {
        ChatSettings chatSettings = chatSettingsRepository.findByChatId(updateGroupNameRequest.chatId());

        if(chatSettings == null) {
            throw new NotFoundException("Chat with id " + updateGroupNameRequest.chatId() + " not found");
        }

        chatSettingsMapper.updateGroupName(chatSettings, updateGroupNameRequest.groupName());

        return chatSettingsMapper.asUpdatedGroupNameResponse(chatSettings);
    }

    @Override
    @Transactional
    @Modifying
    public UpdatedDeliveryTimeResponse updateDeliveryTime(UpdateDeliveryTimeChatSettingsRequest updateDeliveryTimeChatSettingsRequest) throws NotFoundException, ValidationException {
        if (updateDeliveryTimeChatSettingsRequest.deliveryTime() == null) {
            throw new ValidationException("Delivery time can't be null");
        }
        if(updateDeliveryTimeChatSettingsRequest.deliveryTime().getMinute() % 30 != 0) {
            throw new ValidationException("Delivery time must be multiple of 30 minutes");
        }

        ChatSettings chatSettings = chatSettingsRepository.findByChatId(updateDeliveryTimeChatSettingsRequest.chatId());

        if(chatSettings == null) {
            throw new NotFoundException("Chat with id " + updateDeliveryTimeChatSettingsRequest.chatId() + " not found");
        }

        chatSettingsMapper.updateDeliveryTime(chatSettings, updateDeliveryTimeChatSettingsRequest.deliveryTime());

        return chatSettingsMapper.asUpdatedDeliveryTimeResponse(chatSettings);
    }

    @Override
    @Transactional
    @Modifying
    public void removeDeliveryTime(Long chatId) throws NotFoundException {
        ChatSettings chatSettings = chatSettingsRepository.findByChatId(chatId);

        if(chatSettings == null) {
            throw new NotFoundException("Chat with id " + chatId + " not found");
        }

        chatSettingsMapper.updateDeliveryTime(chatSettings, null);
    }

}
