package com.example;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AfterSpringTest {

    @Autowired private EmptyAfter after;
    @Autowired private CacheManager cacheManager;

    @BeforeEach public void reset() {
        after.reset();
        cacheManager.getCache("titlesFromMainPage").clear();
    }

    @Test
    public void noException_oneInvocationOfInnerMethod() {
        after.getTitlesFromMainPage("any-test-url");
        assertEquals(1, after.getNumberOfInvocations());
    }

    @Test
    public void oneException_twoInvocationsOfInnerMethod() {
        after.setNumberOfExceptionsToThrow(1);
        after.getTitlesFromMainPage("any-test-url");
        assertEquals(2, after.getNumberOfInvocations());
    }

    @Test
    public void twoExceptions_threeInvocationsOfInnerMethod() {
        after.setNumberOfExceptionsToThrow(2);
        after.getTitlesFromMainPage("any-test-url");
        assertEquals(3, after.getNumberOfInvocations());
    }

    @Test
    public void threeExceptions_threeInvocationsOfInnerMethod_andThrows()  {
        after.setNumberOfExceptionsToThrow(3);
        assertThrows(RuntimeException.class, () -> after.getTitlesFromMainPage("any-test-url"));
        assertEquals(3, after.getNumberOfInvocations());
    }

    @Test
    public void noException_twoPublicCalls_InvocationsOfInnerMethod() {
        assertEquals(0, ((Map)cacheManager.getCache("titlesFromMainPage").getNativeCache()).size());
        after.getTitlesFromMainPage("any-test-url");

        assertEquals(1, after.getNumberOfInvocations());
        assertEquals(1, ((Map)cacheManager.getCache("titlesFromMainPage").getNativeCache()).size());

        after.getTitlesFromMainPage("any-test-url");

        assertEquals(1, after.getNumberOfInvocations());
        assertEquals(1, ((Map)cacheManager.getCache("titlesFromMainPage").getNativeCache()).size());
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean public EmptyAfter getAfter() { return new EmptyAfter(); }
    }

    @Slf4j
    public static class EmptyAfter extends After {
        @Getter private int numberOfInvocations = 0;
        @Setter private int numberOfExceptionsToThrow = 0;

        void reset() {
            numberOfInvocations = 0;
            numberOfExceptionsToThrow = 0;
        }

        @Override
        List<String> getTitlesFromMainPageInternal(String url) {
            numberOfInvocations++;

            if (numberOfExceptionsToThrow > 0) {
                numberOfExceptionsToThrow--;
                log.info("EmptyAfter throws exception now");
                throw new RuntimeException();
            }

            log.info("Empty after returns empty list now");
            return List.of();
        }
    }

}
