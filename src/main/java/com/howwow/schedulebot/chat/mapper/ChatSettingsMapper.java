package com.howwow.schedulebot.chat.mapper;

import com.howwow.schedulebot.chat.dto.response.*;
import com.howwow.schedulebot.model.entity.ChatSettings;
import org.springframework.stereotype.Component;

import java.time.LocalTime;


@Component
public class ChatSettingsMapper {

    public UpdatedGroupNameChatResponse asUpdatedGroupNameResponse(ChatSettings chatSettings) {
        return UpdatedGroupNameChatResponse.builder()
                .chatId(chatSettings.getChatId())
                .groupName(chatSettings.getGroupName())
                .build();
    }

    public UpdatedDeliveryTimeResponse asUpdatedDeliveryTimeResponse(ChatSettings chatSettings) {
        return UpdatedDeliveryTimeResponse.builder()
                .chatId(chatSettings.getChatId())
                .deliveryTime(chatSettings.getDeliveryTime())
                .build();
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
