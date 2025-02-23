package com.howwow.schedulebot.repository;

import com.howwow.schedulebot.model.entity.ChatSettings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatSettingsRepository extends CommonRepository<ChatSettings>{
    boolean existsChatSettingsByChatId(Long chatId);
    Optional<ChatSettings> findChatSettingsByChatId(Long chatId);
    @Query("SELECT c FROM chat_settings c WHERE c.groupName IS NOT NULL AND c.deliveryTime IS NOT NULL AND c.isActive = TRUE")
    Iterable<ChatSettings> findAllActiveChatSettings();
}
