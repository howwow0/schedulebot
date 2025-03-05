package com.howwow.schedulebot.cite.service;

import com.howwow.schedulebot.cite.dto.request.CreateCiteRequest;
import com.howwow.schedulebot.cite.dto.response.CreatedCiteResponse;
import com.howwow.schedulebot.cite.mapper.CiteMapper;
import com.howwow.schedulebot.exception.chat.ChatNotFoundException;
import com.howwow.schedulebot.model.entity.ChatSettings;
import com.howwow.schedulebot.model.entity.Cite;
import com.howwow.schedulebot.repository.ChatSettingsRepository;
import com.howwow.schedulebot.repository.CiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CiteServiceImpl implements CiteService {
    private final CiteRepository citeRepository;
    private final ChatSettingsRepository chatSettingsRepository;
    private final CiteMapper citeMapper;

    @Override
    public CreatedCiteResponse create(CreateCiteRequest createCiteRequest) throws ChatNotFoundException {
        ChatSettings chat = chatSettingsRepository.findChatSettingsByChatId(createCiteRequest.chatId())
                .orElseThrow(()-> new ChatNotFoundException(createCiteRequest.chatId()));

        Cite cite = Cite.builder()
                .userId(createCiteRequest.userId())
                .chatId(chat)
                .cite(createCiteRequest.cite())
                .build();
        citeRepository.save(cite);

        return citeMapper.asCreatedCiteResponse(cite);
    }
}
