package com.url.shortener.controller;
import java.util.Map;

import com.url.shortener.models.UrlMapping;
import com.url.shortener.service.RateLimiterService;
import com.url.shortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    private UrlService service;
    @Autowired
    private RateLimiterService rateLimiter;

    // 🔹 Create short URL
    @PostMapping("/shorten")
    public ResponseEntity<?> shorten(
            @RequestBody Map<String, String> request,
            HttpServletRequest httpRequest
    ) {

        String userIp = httpRequest.getRemoteAddr();

        if (!rateLimiter.allowRequest(userIp)) {
            return ResponseEntity.status(429)
                    .body("Too many requests. Try again later.");
        }

        String url = request.get("url");
        String shortCode = service.shortenUrl(url);

        return ResponseEntity.ok("http://localhost:8080/api/" + shortCode);
    }

    // 🔹 Redirect to original URL
    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        String originalUrl = service.getOriginalUrl(code);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
    @GetMapping("/stats/{code}")
    public ResponseEntity<Integer> getStats(@PathVariable String code) {

        UrlMapping mapping = service.getMapping(code);  // we'll add this next

        return ResponseEntity.ok(mapping.getClickEvents().size());
    }
}