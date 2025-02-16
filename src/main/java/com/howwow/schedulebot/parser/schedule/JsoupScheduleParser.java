package com.howwow.schedulebot.parser.schedule;

import com.howwow.schedulebot.model.entity.GroupType;
import com.howwow.schedulebot.model.entity.Lesson;
import com.howwow.schedulebot.model.entity.WeekType;
import com.howwow.schedulebot.parser.time.TimeParser;
import com.howwow.schedulebot.parser.utils.DateUtils;
import com.howwow.schedulebot.schedule.fetcher.Fetcher;
import com.howwow.schedulebot.parser.Parser;
import com.howwow.schedulebot.parser.dayofweek.DayOfWeekParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsoupScheduleParser implements Parser<String, List<Lesson>> {

    private static final String NO_LESSONS_TEXT = "Нет пар";
    private final TimeParser timeParser;
    private final DayOfWeekParser dayOfWeekParser;
    private final Fetcher fetcher;

    @Override
    public List<Lesson> parse(String groupName) {
        log.info("Начало парсинга расписания для группы: {}", groupName);
        List<Lesson> lessons = new ArrayList<>();

        try {
            String url = "https://ios.biti.mephi.ru/raspisanie/index.php?gr=" + groupName;
            String html = fetcher.fetch(url);
            Document document = Jsoup.parse(html);
            log.debug("HTML страницы успешно загружен, длина: {} символов", html.length());

            DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();
            WeekType currentWeekType = DateUtils.isEvenWeek() ? WeekType.EVEN : WeekType.ODD;

            Elements rows = document.select("table tbody tr");
            DayOfWeek dayOfWeek = null;

            for (Element row : rows) {
                Elements headers = row.select("th.column-title");
                if (!headers.isEmpty()) {
                    String dayText = headers.getFirst().text();
                    if (dayText.isBlank()) {
                        continue;
                    }
                    dayOfWeek = dayOfWeekParser.parse(dayText);
                    log.debug("Определен день недели: {}", dayOfWeek);
                } else if (dayOfWeek != null && dayOfWeek == currentDayOfWeek) {
                    Elements cells = row.select("td");
                    if (cells.size() >= 7) {
                        if (cells.getFirst().text().equals(NO_LESSONS_TEXT)) {
                            log.warn("В расписании указано 'Нет пар'");
                            return lessons;
                        }

                        Lesson newLesson = parseLesson(cells);
                        if (newLesson != null && isLessonRelevant(newLesson, currentWeekType)) {
                            mergeOrAddLesson(lessons, newLesson);
                            log.debug("Добавлена или объединена пара: {}", newLesson);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("Ошибка при парсинге расписания для группы: {}", groupName, e);
            throw new RuntimeException("Ошибка парсинга расписания", e);
        }

        log.info("Парсинг расписания завершен: найдено {} уроков", lessons.size());
        return lessons;
    }

    private Lesson parseLesson(Elements cells) {
        try {
            String[] timeRange = cells.get(1).text().split(" - ");
            return Lesson.builder()
                    .numberLesson(Integer.parseInt(cells.get(0).text()))
                    .startTime(timeParser.parse(timeRange[0]))
                    .endTime(timeParser.parse(timeRange[1]))
                    .weekType(WeekType.fromString(cells.get(2).text().trim()))
                    .groupType(GroupType.fromString(cells.get(3).text().trim()))
                    .discipline(cells.get(4).text())
                    .professor(cells.get(5).text())
                    .room(cells.get(6).text())
                    .build();
        } catch (Exception e) {
            log.error("Ошибка при парсинге урока: {}", cells.text(), e);
            return null;
        }
    }

    private boolean isLessonRelevant(Lesson lesson, WeekType currentWeekType) {
        return lesson.getWeekType() == WeekType.BOTH || lesson.getWeekType() == currentWeekType;
    }

    private void mergeOrAddLesson(List<Lesson> lessons, Lesson newLesson) {
        Optional<Lesson> existingLesson = lessons.stream()
                .filter(l -> l.getNumberLesson() == newLesson.getNumberLesson()
                        && l.getStartTime().equals(newLesson.getStartTime())
                        && l.getEndTime().equals(newLesson.getEndTime())
                        && l.getWeekType() == newLesson.getWeekType()
                        && l.getGroupType() == newLesson.getGroupType()
                        && l.getDiscipline().equals(newLesson.getDiscipline())
                        && l.getRoom().equals(newLesson.getRoom()))
                .findFirst();

        if (existingLesson.isPresent()) {
            Lesson lesson = existingLesson.get();
            String updatedProfessors = lesson.getProfessor() + ", " + newLesson.getProfessor();
            lesson.setProfessor(updatedProfessors);
        } else {
            lessons.add(newLesson);
        }
    }
}
