package com.howwow.schedulebot.parser.group;

import com.howwow.schedulebot.parser.Parser;
import com.howwow.schedulebot.schedule.fetcher.Fetcher;
import com.howwow.schedulebot.schedule.fetcher.JsoupFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsoupGroupParser implements Parser<String, Set<String>> {

    private final JsoupFetcher fetcher;
    private static final String URL = "https://ios.biti.mephi.ru/raspisanie/";

    @Override
    public Set<String> parse(String unused) {
        log.info("Начало парсинга групп с URL: {}", URL);
        Set<String> groups = new HashSet<>();

        try {
            Document doc = fetcher.parse(URL);
            log.debug("HTML страница успешно загружена");

            Element header = doc.selectFirst("h4:contains(Очная форма обучения)");
            if (header == null) {
                log.warn("Раздел 'Очная форма обучения' не найден на странице");
                throw new IllegalStateException("Раздел 'Очная форма обучения' не найден");
            }

            Element parent = header.parent();
            if (parent != null) {
                Elements links = parent.select("a[href*=index.php?gr=]");
                for (Element link : links) {
                    String href = link.attr("href");
                    if (href.contains("gr=")) {
                        String group = href.split("gr=")[1];
                        groups.add(group);
                        log.debug("Найдена группа: {}", group);
                    }
                }
            }

            if (groups.isEmpty()) {
                log.warn("Парсер не нашел ни одной группы на странице");
                throw new IllegalStateException("Группы не найдены на странице");
            }

            log.info("Парсинг завершен, найдено групп: {}", groups.size());
            return groups;

        } catch (Exception e) {
            log.error("Ошибка при парсинге групп с URL: {}", URL, e);
            throw new RuntimeException("Ошибка при парсинге групп", e);
        }
    }
}
