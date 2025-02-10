package com.howwow.schedulebot.schedule.utils;

import java.io.IOException;

public interface Fetcher {
    String fetch(String url) throws IOException;
}
