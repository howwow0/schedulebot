package com.howwow.schedulebot.cite.service;

import com.howwow.schedulebot.cite.dto.request.CreateCiteRequest;
import com.howwow.schedulebot.cite.dto.response.CreatedCiteResponse;
import com.howwow.schedulebot.exception.chat.ChatNotFoundException;
import jakarta.validation.Valid;

public interface CiteService {
    CreatedCiteResponse create(@Valid CreateCiteRequest createCiteRequest) throws ChatNotFoundException;
}
