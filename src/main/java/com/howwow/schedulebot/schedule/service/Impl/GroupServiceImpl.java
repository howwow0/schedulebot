package com.howwow.schedulebot.schedule.service.Impl;

import com.howwow.schedulebot.model.repository.GroupRepository;
import com.howwow.schedulebot.schedule.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Override
    public boolean isGroupExist(String groupName) {
        return groupRepository.getAllGroups().contains(groupName);
    }
}
