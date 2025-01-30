package com.howwow.schedulebot.model.repository;

import com.howwow.schedulebot.model.entities.Lesson;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends KeyValueRepository<Lesson, String> {
   List<Lesson> findByGroupName(String groupName);
}
