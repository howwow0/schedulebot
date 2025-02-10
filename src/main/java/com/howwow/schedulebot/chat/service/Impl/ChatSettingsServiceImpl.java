package com.howwow.schedulebot.chat.service.Impl;

import com.howwow.schedulebot.chat.dto.request.*;
import com.howwow.schedulebot.chat.dto.response.*;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.chat.mapper.ChatSettingsMapper;
import com.howwow.schedulebot.exception.AlreadyExistsException;
import com.howwow.schedulebot.exception.ValidationException;
import com.howwow.schedulebot.model.entities.ChatSettings;
import com.howwow.schedulebot.model.repository.ChatSettingsRepository;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
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
    public CreatedChatResponse create(CreateChatSettingsRequest createChatSettingsRequest) throws AlreadyExistsException {
        ChatSettings chatSettings = ChatSettings.builder()
                .chatId(createChatSettingsRequest.chatId())
                .build();

        if (chatSettingsRepository.existsByChatId(chatSettings.getChatId()))
            throw new AlreadyExistsException("Chat with id " + chatSettings.getChatId() + " already exists");

        chatSettingsRepository.save(chatSettings);

        return chatSettingsMapper.asCreatedResponse(chatSettings);
    }

    @Override
    public FoundedChatResponse findByChatId(FindChatRequest chatRequest) throws NotFoundException {
        ChatSettings chatSettings = chatSettingsRepository.findByChatId(chatRequest.chatId());

        if(chatSettings == null) {
            throw new NotFoundException("Chat with id " + chatRequest.chatId() + " not found");
        }

        return chatSettingsMapper.asFoundChatResponse(chatSettings);
    }

    @Override
    @Transactional
    @Modifying
    public UpdatedChatResponse updateMessageThreadId(UpdateChatSettingsRequest updateChatSettingsRequest) throws NotFoundException {

        ChatSettings chatSettings = chatSettingsRepository.findByChatId(updateChatSettingsRequest.chatId());

        if(chatSettings == null) {
            throw new NotFoundException("Chat with id " + updateChatSettingsRequest.chatId() + " not found");
        }

        chatSettingsMapper.updateMessageThreadId(chatSettings, updateChatSettingsRequest.messageThreadId());

        return chatSettingsMapper.asUpdatedMessageThreadIdResponse(chatSettings);
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
    public void removeDeliveryTime(RemoveDeliveryTimeChatSettingsRequest removeDeliveryTimeChatSettingsRequest) throws NotFoundException {
        ChatSettings chatSettings = chatSettingsRepository.findByChatId(removeDeliveryTimeChatSettingsRequest.chatId());

        if(chatSettings == null) {
            throw new NotFoundException("Chat with id " + removeDeliveryTimeChatSettingsRequest.chatId() + " not found");
        }

        chatSettingsMapper.updateDeliveryTime(chatSettings, null);
    }

}
