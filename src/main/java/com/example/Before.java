package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class Before {

    @Retryable
    @Cacheable("titlesFromMainPage")
    public List<String> getTitlesFromMainPage(final String url) {
        RestTemplate restTemplate = new RestTemplate();

        log.info("Going to fire up a request against {}", url);
        final var responseEntity = restTemplate.getForEntity(url, String.class);

        final var content = responseEntity.getBody();

        final Pattern pattern = Pattern.compile("<p class=\"resource-title\">\n(.*)\n.*</p>");
        final Matcher matcher = pattern.matcher(content);

        final List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group(1).trim());
        }

        log.info("Found titles: {}", result);
        return result;
    }
}
