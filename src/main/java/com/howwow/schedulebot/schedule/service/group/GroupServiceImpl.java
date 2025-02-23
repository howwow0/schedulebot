package com.howwow.schedulebot.schedule.service.group;

import com.howwow.schedulebot.parser.group.JsoupGroupParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final JsoupGroupParser jsoupGroupParser;

    @Override
    public boolean isGroupExist(String groupName) {
        log.info("Выполняется проверка существования группы: {}", groupName);
        return jsoupGroupParser.parse("fetch")
                .stream()
                .anyMatch(group -> group.equals(groupName));
    }
}
