package com.howwow.schedulebot.model.repository.Impl;

import com.howwow.schedulebot.model.repository.GroupRepository;
import com.howwow.schedulebot.schedule.utils.GroupParser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryImpl implements GroupRepository {
    private final GroupParser groupParser;

    @Override
    @Cacheable("groups")
    public Set<String> getAllGroups() {
        return groupParser.parse();
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    @CacheEvict(value = "groups", allEntries = true)
    public void refreshGroupsCache() {
    }
}
