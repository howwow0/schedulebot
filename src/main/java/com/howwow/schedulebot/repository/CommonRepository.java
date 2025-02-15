package com.howwow.schedulebot.repository;

import com.howwow.schedulebot.model.entity.AbstractEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface CommonRepository<T extends AbstractEntity> extends CrudRepository<T, Long> {
}
