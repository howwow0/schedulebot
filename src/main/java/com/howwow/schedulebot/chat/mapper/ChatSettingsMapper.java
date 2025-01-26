package com.howwow.schedulebot.chat.mapper;

import com.howwow.schedulebot.chat.dto.response.*;
import com.howwow.schedulebot.model.entities.ChatSettings;
import org.springframework.stereotype.Component;

import java.time.LocalTime;


@Component
public class ChatSettingsMapper {
    public CreatedChatResponse asCreatedResponse(ChatSettings chatSettings) {
        return new CreatedChatResponse(chatSettings.getChatId());
    }

    public UpdatedChatResponse asUpdatedMessageThreadIdResponse(ChatSettings chatSettings) {
        return new UpdatedChatResponse(chatSettings.getChatId(), chatSettings.getMessageThreadId());
    }

    public UpdatedGroupNameChatResponse asUpdatedGroupNameResponse(ChatSettings chatSettings) {
        return new UpdatedGroupNameChatResponse(chatSettings.getChatId(), chatSettings.getGroupName());
    }

    public UpdatedDeliveryTimeResponse asUpdatedDeliveryTimeResponse(ChatSettings chatSettings) {
        return new UpdatedDeliveryTimeResponse(chatSettings.getChatId(), chatSettings.getDeliveryTime());
    }

    public FoundedChatResponse asFoundChatResponse(ChatSettings chatSettings){
        return FoundedChatResponse.builder()
                .chatId(chatSettings.getChatId())
                .deliveryTime(chatSettings.getDeliveryTime())
                .groupName(chatSettings.getGroupName()).build();
    }

    public void updateMessageThreadId(ChatSettings chatSettings, Integer messageThreadId) {
        chatSettings.setMessageThreadId(messageThreadId);
    }

    public void updateGroupName(ChatSettings chatSettings, String groupName){
        chatSettings.setGroupName(groupName);
    }

    public void updateDeliveryTime(ChatSettings chatSettings, LocalTime deliveryTime){
        chatSettings.setDeliveryTime(deliveryTime);
    }

}
