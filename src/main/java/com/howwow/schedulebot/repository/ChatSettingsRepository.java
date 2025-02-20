package com.howwow.schedulebot.repository;

import com.howwow.schedulebot.model.entity.ChatSettings;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public interface ChatSettingsRepository extends CommonRepository<ChatSettings>{
    boolean existsChatSettingsByChatId(Long chatId);

    Optional<ChatSettings> findChatSettingsByChatId(Long chatId);

    default Map<String, List<ChatSettings>> findGroupingByGroupName() {
        return StreamSupport.stream(findAll().spliterator(), false)
                .filter(p-> Objects.nonNull(p.getGroupName()) && Strings.isNotBlank(p.getGroupName()))
                .collect(Collectors.groupingBy(ChatSettings::getGroupName));
    }

}
