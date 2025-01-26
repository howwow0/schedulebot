package com.howwow.schedulebot.model.repository;

import com.howwow.schedulebot.model.entities.ChatSettings;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSettingsRepository extends CommonRepository<ChatSettings>{
    boolean existsByChatId(Long chatId);
    void deleteByChatId(Long chatId);
    ChatSettings findByChatId(Long chatId);
}
