package com.howwow.schedulebot.telegram.commands.service;

import com.howwow.schedulebot.schedule.utils.GroupParser;
import com.howwow.schedulebot.schedule.utils.LessonFormatter;
import com.howwow.schedulebot.schedule.utils.ScheduleParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Set;

@Component
public class TestCommand extends ServiceCommand{

    private final GroupParser groupParser;
    private final ScheduleParser parser;
    private final LessonFormatter lessonFormatter;

    public TestCommand(GroupParser groupParser, ScheduleParser parser, LessonFormatter lessonFormatter) {
        super("test", "Test command");
        this.groupParser = groupParser;
        this.parser = parser;
        this.lessonFormatter = lessonFormatter;
    }

    @Override
    protected void execute(AbsSender absSender, User user, Integer messageThreadId, Chat chat, String[] strings) {
        Set<String> groups = groupParser.parse();
        sendAnswer(absSender, chat.getId(), messageThreadId, this.getCommandIdentifier(),
                lessonFormatter.format(
                parser.parse("АЭС-31")));
    }
}
