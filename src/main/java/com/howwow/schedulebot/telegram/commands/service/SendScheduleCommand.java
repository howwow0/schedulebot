package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.chat.dto.response.FoundedChatResponse;
import com.howwow.schedulebot.chat.service.ChatSettingsService;
import com.howwow.schedulebot.schedule.service.lesson.LessonService;
import com.howwow.schedulebot.telegram.commands.BotCommands;
import com.howwow.schedulebot.telegram.exception.handlers.ChatCommandExceptionHandler;
import com.howwow.schedulebot.telegram.exception.handlers.ValidationExceptionHandler;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;


@Component
@Slf4j
public final class SendScheduleCommand extends ServiceCommand {

    private final ChatSettingsService chatSettingsService;
    private final LessonService lessonService;

    public SendScheduleCommand(ChatSettingsService chatSettingsService, LessonService lessonService) {
        super(BotCommands.SCHEDULE.toString(), "Получить расписание на сегодня 📅");
        this.chatSettingsService = chatSettingsService;
        this.lessonService = lessonService;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        log.info("Пользователь '{}' пытается получить расписание в чате {}", user.getUserName(), chat.getId());

        try {
            FoundedChatResponse foundedChatResponse = chatSettingsService.findByChatId(chat.getId());
            String formattedLessons = lessonService.getFormattedLessonsForGroup(foundedChatResponse.groupName());
            sendAnswer(absSender, chat.getId(), messageThreadId, formattedLessons);
            log.info("Расписание успешно отправлено для чата {}", chat.getId());
        } catch (ConstraintViolationException e) {
            ValidationExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        } catch (Exception e) {
            ChatCommandExceptionHandler.handleException(absSender, chat, user, messageThreadId, e, getCommandIdentifier());
        }
    }
}
