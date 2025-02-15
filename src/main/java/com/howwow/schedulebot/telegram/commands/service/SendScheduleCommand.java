package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.chat.dto.response.FoundedChatResponse;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.exception.NotFoundException;
import com.howwow.schedulebot.model.entity.Lesson;
import com.howwow.schedulebot.schedule.service.lesson.LessonService;
import com.howwow.schedulebot.schedule.service.lesson.dto.request.FindLessonByGroupNameRequest;
import com.howwow.schedulebot.schedule.service.lesson.utils.LessonFormatter;
import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.config.MessageTemplates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
@Slf4j
public final class SendScheduleCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;
    private final LessonService lessonService;
    private final LessonFormatter lessonFormatter;

    public SendScheduleCommand(ChatSettingsService chatSettingsService, LessonService lessonService, LessonFormatter lessonFormatter) {
        super(BotCommands.SCHEDULE.toString(), "Получить расписание на сегодня 📅");
        this.chatSettingsService = chatSettingsService;
        this.lessonService = lessonService;
        this.lessonFormatter = lessonFormatter;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' пытается получить расписание в чате {}", user.getUserName(), chat.getId());

        try {
            FoundedChatResponse foundedChatResponse = chatSettingsService.findByChatId(chat.getId());
            if(foundedChatResponse.groupName() == null || foundedChatResponse.groupName().isBlank()){
                log.warn("Для чата {} не указано название группы", chat.getId());
                String errorText = MessageTemplates.GROUP_NAME_NOT_FOUND_ERROR.formatted(BotCommands.UP_GROUP_NAME);
                sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
                return;
            }

            List<Lesson> lessons = lessonService.findLessonsByGroupName(FindLessonByGroupNameRequest.builder()
                    .groupName(foundedChatResponse.groupName())
                    .build());

            sendAnswer(absSender, chat.getId(), messageThreadId, lessonFormatter.format(lessons));

            log.info("Расписание успешно отправлено для чата {}", chat.getId());
        } catch (NotFoundException e) {
            log.warn("Чат с ID {} не найден", chat.getId());
            String errorText = MessageTemplates.CHAT_NOT_FOUND_ERROR.formatted(BotCommands.START);
            sendAnswer(absSender, chat.getId(), messageThreadId, errorText);
        } catch (Exception e) {
            log.error("Ошибка при отправке расписания в чат {}: {}", chat.getId(), e.getMessage(), e);
            sendAnswer(absSender, chat.getId(), messageThreadId, MessageTemplates.INTERNAL_ERROR);
        }
    }
}
