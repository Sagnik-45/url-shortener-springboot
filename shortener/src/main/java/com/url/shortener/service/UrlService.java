package com.url.shortener.service;

import com.url.shortener.models.ClickEvent;
import com.url.shortener.models.UrlMapping;
import com.url.shortener.repository.ClickEventRepository;
import com.url.shortener.repository.UrlMappingRepository;
import com.url.shortener.util.Base62Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UrlService {
    @Autowired
    private ClickEventRepository clickRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UrlMappingRepository urlRepository;

    public String shortenUrl(String originalUrl) {

        UrlMapping entity = new UrlMapping();
        entity.setOriginalUrl(originalUrl);
        entity.setCreatedDate(LocalDateTime.now());

        // Step 1: Save to get ID
        entity = urlRepository.save(entity);

        // Step 2: Generate short code
        String shortCode = Base62Encoder.encode(entity.getId());

        entity.setShortCode(shortCode);

        // Step 3: Save again
        urlRepository.save(entity);
        System.out.println("Saved ID: " + entity.getId());
        System.out.println("Saved shortCode: " + shortCode);
        System.out.println("Saved originalUrl: " + entity.getOriginalUrl());

        return shortCode;
    }

    public String getOriginalUrl(String shortCode) {

        UrlMapping mapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        // Track click
        ClickEvent click = new ClickEvent();
        click.setUrlMapping(mapping);
        click.setClickDate(LocalDateTime.now());

        clickRepository.save(click);

        return mapping.getOriginalUrl();
    }

    public UrlMapping getMapping(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("URL not found"));
    }
}
