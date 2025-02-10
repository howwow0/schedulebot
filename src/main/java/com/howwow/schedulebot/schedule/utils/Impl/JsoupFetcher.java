package com.howwow.schedulebot.schedule.utils.Impl;

import com.howwow.schedulebot.schedule.utils.Fetcher;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsoupFetcher implements Fetcher {
    @Override
    public String fetch(String url) throws IOException {
       return Jsoup.connect(url).get().html();
    }
}
