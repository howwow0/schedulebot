package com.howwow.schedulebot.schedule.service.group;

import com.howwow.schedulebot.parser.group.JsoupGroupParser;
import com.howwow.schedulebot.schedule.service.group.dto.request.GroupCheckRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final JsoupGroupParser jsoupGroupParser;

    @Override
    @Cacheable(value = "groupExistCache", key = "#groupCheckRequest.groupName", unless = "#result == false")
    public boolean isGroupExist(GroupCheckRequest groupCheckRequest) {
        log.info("Выполняется проверка существования группы: {}", groupCheckRequest.groupName());
        return jsoupGroupParser.parse("fetch")
                .stream()
                .anyMatch(group -> group.equals(groupCheckRequest.groupName()));
    }
}
