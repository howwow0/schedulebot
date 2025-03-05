package com.howwow.schedulebot.cite.mapper;

import com.howwow.schedulebot.cite.dto.response.CreatedCiteResponse;
import com.howwow.schedulebot.model.entity.Cite;
import org.springframework.stereotype.Component;

@Component
public class CiteMapper {

    public CreatedCiteResponse asCreatedCiteResponse(Cite cite) {
        return CreatedCiteResponse.builder()
                .userId(cite.getUserId())
                .build();
    }
}
