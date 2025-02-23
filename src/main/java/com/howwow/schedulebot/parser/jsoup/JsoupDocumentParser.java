package com.howwow.schedulebot.parser.jsoup;

import com.howwow.schedulebot.parser.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsoupDocumentParser implements Parser<String, Document> {
    private static final int TIMEOUT_MILLIS = 5000;
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; ScheduleBot/1.0)";

    @Override
    public Document parse(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MILLIS)
                    .ignoreContentType(true)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке страницы: " + url, e);
        }
    }
}
