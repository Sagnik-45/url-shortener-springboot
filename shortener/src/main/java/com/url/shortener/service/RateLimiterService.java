package com.url.shortener.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RateLimiterService {

    private final Map<String, Integer> requestCount = new HashMap<>();
    private final Map<String, Long> lastRequestTime = new HashMap<>();

    private static final int LIMIT = 5; // max requests
    private static final long TIME_WINDOW = 60 * 1000; // 1 minute

    public boolean allowRequest(String user) {

        long currentTime = System.currentTimeMillis();

        lastRequestTime.putIfAbsent(user, currentTime);

        if (currentTime - lastRequestTime.get(user) > TIME_WINDOW) {
            requestCount.put(user, 0);
            lastRequestTime.put(user, currentTime);
        }

        requestCount.put(user, requestCount.getOrDefault(user, 0) + 1);

        return requestCount.get(user) <= LIMIT;
    }
}