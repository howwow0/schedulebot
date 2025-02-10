package com.howwow.schedulebot.schedule.utils.Impl;

import com.howwow.schedulebot.schedule.utils.Fetcher;
import com.howwow.schedulebot.schedule.utils.GroupParser;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GroupParserImpl implements GroupParser {
    private final Fetcher fetcher;

    @Override
    public Set<String> parse() {
        Set<String> groups = new HashSet<>();
        try {
            String url = "https://ios.biti.mephi.ru/raspisanie/";
            String html = fetcher.fetch(url);
            Document doc = Jsoup.parse(html);

            Element header = doc.selectFirst("h4:contains(Очная форма обучения)");
            if (header == null) {
                throw new RuntimeException("Раздел 'Очная форма обучения' не найден");
            }

            Element parent = header.parent();
            if (parent != null) {
                Elements links = parent.select("a[href*=index.php?gr=]");
                for (Element link : links) {
                    String href = link.attr("href");
                    if (href.contains("gr=")) {
                        String groupName = href.split("gr=")[1];
                        groups.add(groupName);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке страницы", e);
        }
        return groups;
    }
}
