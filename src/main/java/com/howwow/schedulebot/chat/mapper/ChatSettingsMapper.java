package com.howwow.schedulebot.chat.mapper;

import com.howwow.schedulebot.chat.dto.response.*;
import com.howwow.schedulebot.model.entity.ChatSettings;
import com.howwow.schedulebot.chat.utils.SettingsFormatter;
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

    public CreatedChatResponse asCreatedChatResponse(ChatSettings chatSettings){
        return CreatedChatResponse.builder()
                .chatId(chatSettings.getChatId())
                .build();
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

    public UpdatedMessageThreadIdResponse asUpdatedMessageThreadIdResponse(ChatSettings chatSettings) {
        return UpdatedMessageThreadIdResponse.builder()
                .chatId(chatSettings.getChatId())
                .messageThreadId(chatSettings.getMessageThreadId())
                .build();
    }

    public UpdatedIsActiveStatusResponse asUpdatedIsActiveStatusResponse(ChatSettings chatSettings) {
        return UpdatedIsActiveStatusResponse.builder()
                .chatId(chatSettings.getChatId())
                .isActive(chatSettings.getIsActive())
                .build();
    }

    public void toggleIsActive(ChatSettings chatSettings) {
        chatSettings.setIsActive(!chatSettings.getIsActive());
    }

    public String format(ChatSettings chatSettings, SettingsFormatter settingsFormatter) {
        return settingsFormatter.format(chatSettings);
    }
}
