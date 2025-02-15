package com.howwow.schedulebot.schedule.fetcher;

import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsoupFetcher implements Fetcher {
    private static final int TIMEOUT_MILLIS = 5000;
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; ScheduleBot/1.0)";

    @Override
    public String fetch(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT_MILLIS)
                    .ignoreContentType(true)
                    .get()
                    .html();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке страницы: " + url, e);
        }
    }
}
