package com.howwow.schedulebot.model.repository;

import com.howwow.schedulebot.model.entities.ChatSettings;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public interface ChatSettingsRepository extends CommonRepository<ChatSettings>{
    boolean existsByChatId(Long chatId);
    ChatSettings findByChatId(Long chatId);

    default Map<String, List<ChatSettings>> findGroupingByGroupName() {
        return StreamSupport.stream(findAll().spliterator(), false)
                .collect(Collectors.groupingBy(ChatSettings::getGroupName));
    }

}
